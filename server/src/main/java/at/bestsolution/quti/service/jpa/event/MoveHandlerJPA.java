package at.bestsolution.quti.service.jpa.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.model.modification.EventModificationMovedEntity;
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
public class MoveHandlerJPA extends BaseHandler implements EventService.MoveHandler {

	@Inject
	public MoveHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> move(String calendarKey, String eventKey, ZonedDateTime start, ZonedDateTime end) {
		var seriesSep = eventKey.indexOf('_');

		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = seriesSep == -1 ? Utils.parseUUID(eventKey, "in path")
				: Utils.parseUUID(eventKey.substring(0, seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? Result.<LocalDate>ok(null)
				: Utils.parseLocalDate(eventKey.substring(seriesSep + 1), "in path");

		if (parsedCalendarKey.isNotOk()) {
			return parsedCalendarKey.toAny();
		}
		if (parsedEventKey.isNotOk()) {
			return parsedEventKey.toAny();
		}
		if (parsedOriginalDate.isNotOk()) {
			return parsedOriginalDate.toAny();
		}

		if (parsedOriginalDate.value() == null) {
			return moveSingleEvent(parsedCalendarKey.value(), parsedEventKey.value(), start, end);
		}
		return moveEventInSeries(parsedCalendarKey.value(), parsedEventKey.value(), parsedOriginalDate.value(), start, end);
	}

	private Result<Void> moveSingleEvent(UUID calendarKey, UUID eventKey, ZonedDateTime start, ZonedDateTime end) {
		var em = em();

		var event = EventUtils.event(em, calendarKey, eventKey);

		if (event == null) {
			return Result.notFound("No event with master-key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		event.start = event.fullday ? Utils.atStartOfDay(start) : start;
		event.end = event.fullday ? Utils.atEndOfDay(end) : end;
		em.persist(event);

		return Result.OK;
	}

	private Result<Void> moveEventInSeries(UUID calendarKey, UUID eventKey, LocalDate original, ZonedDateTime start,
			ZonedDateTime end) {
		var em = em();

		var event = EventUtils.event(em, calendarKey, eventKey);

		if (event == null) {
			return Result.notFound("No event with master-key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		var startDatetime = ZonedDateTime.of(original, LocalTime.MIN, event.repeatPattern.recurrenceTimezone);
		var endDatetime = ZonedDateTime.of(original, LocalTime.MAX, event.repeatPattern.recurrenceTimezone);

		if (!RepeatUtils.fromRepeat(event, startDatetime, endDatetime).anyMatch(d -> d.equals(original))) {
			return Result.notFound("Event is not repeated on the given date %s", original);
		}

		var entity = event.modifications.stream()
				.filter(e -> e instanceof EventModificationMovedEntity)
				.map(e -> (EventModificationMovedEntity) e)
				.filter(e -> e.date.equals(original))
				.findFirst()
				.orElseGet(() -> new EventModificationMovedEntity());

		entity.date = original;
		entity.event = event;
		entity.start = event.fullday ? Utils.atStartOfDay(start) : start;
		entity.end = event.fullday ? Utils.atEndOfDay(end) : end;

		em.persist(entity);

		return Result.OK;
	}
}
