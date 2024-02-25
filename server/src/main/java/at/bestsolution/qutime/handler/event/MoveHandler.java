package at.bestsolution.qutime.handler.event;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import at.bestsolution.qutime.Utils.Result;
import at.bestsolution.qutime.handler.BaseHandler;
import at.bestsolution.qutime.model.modification.EventModificationMovedEntity;
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
			return Result.notFound("No event with key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		var entity = event.modifications.stream()
			.filter(e -> e instanceof EventModificationMovedEntity)
			.map(e -> (EventModificationMovedEntity)e)
			.filter( e -> e.date == original)
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
