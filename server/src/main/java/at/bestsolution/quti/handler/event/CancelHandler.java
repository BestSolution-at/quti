package at.bestsolution.quti.handler.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import at.bestsolution.quti.Utils.Result;
import at.bestsolution.quti.handler.BaseHandler;
import at.bestsolution.quti.handler.RepeatUtils;
import at.bestsolution.quti.model.modification.EventModificationCanceledEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CancelHandler extends BaseHandler {

	@Inject
	public CancelHandler(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> cancel(UUID calendarKey, UUID eventKey, LocalDate original) {
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
			.filter(e -> e instanceof EventModificationCanceledEntity)
			.map(e -> (EventModificationCanceledEntity)e)
			.filter( e -> e.date.equals(original))
			.findFirst()
			.orElseGet( () -> new EventModificationCanceledEntity());

		entity.date = original;
		entity.event = event;

		em.persist(entity);

		return Result.OK;
	}
}
