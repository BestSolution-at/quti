package at.bestsolution.quti.service.jpa.event;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class DeleteHandlerImpl extends BaseHandler implements EventService.DeleteHandler {

	@Inject
	public DeleteHandlerImpl(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> delete(String calendarKey, String eventKey) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

		if( parsedCalendarKey.isNotOk() ) {
			return parsedCalendarKey.toAny();
		}

		if( parsedEventKey.isNotOk() ) {
			return parsedEventKey.toAny();
		}

		var em = em();
		var event = EventUtils.event(em, parsedCalendarKey.value(), parsedEventKey.value());
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