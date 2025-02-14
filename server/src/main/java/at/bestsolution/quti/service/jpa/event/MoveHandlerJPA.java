package at.bestsolution.quti.service.jpa.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.jpa.model.modification.EventModificationMovedEntity;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.NotFoundException;
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
	public void move(BuilderFactory factory, String calendarKey, String eventKey, ZonedDateTime start,
			ZonedDateTime end) {
		var seriesSep = eventKey.indexOf('_');

		var parsedCalendarKey = Utils._parseUUID(calendarKey, "in path");
		var parsedEventKey = seriesSep == -1 ? Utils._parseUUID(eventKey, "in path")
				: Utils._parseUUID(eventKey.substring(0, seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? null
				: Utils._parseLocalDate(eventKey.substring(seriesSep + 1), "in path");

		if (parsedOriginalDate == null) {
			moveSingleEvent(parsedCalendarKey, parsedEventKey, start, end);
		} else {
			moveEventInSeries(parsedCalendarKey, parsedEventKey, parsedOriginalDate, start, end);
		}
	}

	private void moveSingleEvent(UUID calendarKey, UUID eventKey, ZonedDateTime start, ZonedDateTime end) {
		var em = em();

		var event = EventUtils.event(em, calendarKey, eventKey);

		if (event == null) {
			throw new NotFoundException(
					"No event with master-key '%s' was found in calendar '%s'".formatted(eventKey, calendarKey));
		}

		event.start = event.fullday ? Utils.atStartOfDay(start) : start;
		event.end = event.fullday ? Utils.atEndOfDay(end) : end;
		em.persist(event);
	}

	private void moveEventInSeries(UUID calendarKey, UUID eventKey, LocalDate original, ZonedDateTime start,
			ZonedDateTime end) {
		var em = em();

		var event = EventUtils.event(em, calendarKey, eventKey);

		if (event == null) {
			throw new NotFoundException(
					"No event with master-key '%s' was found in calendar '%s'".formatted(eventKey, calendarKey));
		}

		var startDatetime = ZonedDateTime.of(original, LocalTime.MIN, event.repeatPattern.recurrenceTimezone);
		var endDatetime = ZonedDateTime.of(original, LocalTime.MAX, event.repeatPattern.recurrenceTimezone);

		if (!RepeatUtils.fromRepeat(event, startDatetime, endDatetime).anyMatch(d -> d.equals(original))) {
			throw new NotFoundException("Event is not repeated on the given date %s".formatted(original));
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
	}
}
