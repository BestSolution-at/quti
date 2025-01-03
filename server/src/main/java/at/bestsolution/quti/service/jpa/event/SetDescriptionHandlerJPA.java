package at.bestsolution.quti.service.jpa.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.jpa.model.modification.EventModificationGenericEntity;
import at.bestsolution.quti.service.DTOBuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.RepeatUtils;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class SetDescriptionHandlerJPA extends BaseHandler implements EventService.SetDescriptionHandler {
	@Inject
	public SetDescriptionHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> setDescription(DTOBuilderFactory factory, String calendarKey, String eventKey, String description) {
		var seriesSep = eventKey.indexOf('_');

		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = seriesSep == -1 ? Utils.parseUUID(eventKey, "in path") : Utils.parseUUID(eventKey.substring(0,seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? Result.<LocalDate>ok(null) : Utils.parseLocalDate(eventKey.substring(seriesSep+1), "in path");

		if( parsedCalendarKey.isNotOk() ) {
			return parsedCalendarKey.toAny();
		}
		if( parsedEventKey.isNotOk() ) {
			return parsedEventKey.toAny();
		}
		if( parsedOriginalDate.isNotOk() ) {
			return parsedOriginalDate.toAny();
		}

		if( parsedOriginalDate.value() == null ) {
			return setDescriptionSingleEvent(parsedCalendarKey.value(), parsedEventKey.value(), description);
		}
		return setDescriptionInSeries(parsedCalendarKey.value(), parsedEventKey.value(), parsedOriginalDate.value(), description);
	}

	private Result<Void> setDescriptionSingleEvent(UUID calendarKey, UUID eventKey, String description) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);
		if( event == null ) {
			return Result.notFound("No event with master-key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		event.description = description;
		em.persist(event);

		return Result.OK;
	}

	private Result<Void> setDescriptionInSeries(UUID calendarKey, UUID eventKey, LocalDate original, String description) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		var startDatetime = ZonedDateTime.of(original, LocalTime.MIN, event.repeatPattern.recurrenceTimezone);
		var endDatetime = ZonedDateTime.of(original, LocalTime.MAX, event.repeatPattern.recurrenceTimezone);

		if( ! RepeatUtils.fromRepeat(event, startDatetime, endDatetime).anyMatch( d -> d.equals(original)) ) {
			return Result.notFound("Event is not repeated on the given date %s", original);
		}

		var entity = event.modifications.stream()
			.filter(e -> e instanceof EventModificationGenericEntity)
			.map( e -> (EventModificationGenericEntity)e)
			.filter( e -> e.date.equals(original))
			.findFirst()
			.orElseGet( () -> new EventModificationGenericEntity());

		entity.date = original;
		entity.event = event;
		entity.description = description;

		em.persist(entity);

		return Result.OK;
	}
}
