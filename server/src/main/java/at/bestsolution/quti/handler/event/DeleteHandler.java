package at.bestsolution.quti.handler.event;

import java.util.UUID;

import at.bestsolution.quti.Utils.Result;
import at.bestsolution.quti.handler.BaseHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class DeleteHandler extends BaseHandler {

	@Inject
	public DeleteHandler(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> delete(UUID calendarKey, UUID eventKey) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);
		if( event == null ) {
			return Result.notFound("No event with key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}
		event.modifications.forEach(em::remove);
		event.references.forEach(em::remove);
		if( event.repeatPattern != null ) {
			em.remove(event.repeatPattern);
		}
		em.remove(event);
		return Result.OK;
	}
}
