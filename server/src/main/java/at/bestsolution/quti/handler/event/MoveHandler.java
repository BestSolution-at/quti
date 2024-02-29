package at.bestsolution.quti.handler.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import at.bestsolution.quti.Utils.Result;
import at.bestsolution.quti.handler.BaseHandler;
import at.bestsolution.quti.handler.RepeatUtils;
import at.bestsolution.quti.model.modification.EventModificationMovedEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class MoveHandler extends BaseHandler {

	@Inject
	public MoveHandler(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> move(UUID calendarKey, UUID eventKey, LocalDate original, ZonedDateTime start, ZonedDateTime end) {
		var em = em();

		var event = EventUtils.event(em, calendarKey, eventKey);

		if( event == null ) {
			return Result.notFound("No event with master-key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		var startDatetime = ZonedDateTime.of(original, LocalTime.MIN, event.repeatPattern.recurrenceTimezone);
		var endDatetime = ZonedDateTime.of(original, LocalTime.MAX, event.repeatPattern.recurrenceTimezone);

		if( ! RepeatUtils.fromRepeat(event, startDatetime, endDatetime).anyMatch( d -> d.equals(original)) ) {
			return Result.notFound("Event is not repeated on the given date %s", original);
		}

		var entity = event.modifications.stream()
			.filter(e -> e instanceof EventModificationMovedEntity)
			.map(e -> (EventModificationMovedEntity)e)
			.filter( e -> e.date.equals(original))
			.findFirst()
			.orElseGet( () -> new EventModificationMovedEntity());

		entity.date = original;
		entity.event = event;
		entity.start = start;
		entity.end = end;

		em.persist(entity);

		return Result.OK;
	}
}
