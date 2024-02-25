package at.bestsolution.qutime.handler.event;

import java.time.LocalDate;
import java.util.UUID;

import at.bestsolution.qutime.Utils.Result;
import at.bestsolution.qutime.handler.BaseHandler;
import at.bestsolution.qutime.model.modification.EventModificationCanceledEntity;
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
			return Result.notFound("No event with key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		var entity = event.modifications.stream()
			.filter(e -> e instanceof EventModificationCanceledEntity)
			.map(e -> (EventModificationCanceledEntity)e)
			.filter( e -> e.date == original)
			.findFirst()
			.orElseGet( () -> new EventModificationCanceledEntity());

		entity.date = original;
		entity.event = event;

		em.persist(entity);

		return Result.OK;
	}
}
