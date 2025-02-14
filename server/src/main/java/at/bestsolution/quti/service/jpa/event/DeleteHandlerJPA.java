package at.bestsolution.quti.service.jpa.event;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.NotFoundException;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class DeleteHandlerJPA extends BaseHandler implements EventService.DeleteHandler {

	@Inject
	public DeleteHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void delete(BuilderFactory factory, String calendarKey, String eventKey) {
		var parsedCalendarKey = Utils._parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils._parseUUID(eventKey, "in path");

		var em = em();
		var event = EventUtils.event(em, parsedCalendarKey, parsedEventKey);
		if (event == null) {
			throw new NotFoundException("No event with key '%s' was found in calendar '%s'".formatted(eventKey, calendarKey));
		}
		event.modifications.forEach(em::remove);
		event.references.forEach(em::remove);
		if (event.repeatPattern != null) {
			em.remove(event.repeatPattern);
		}
		em.remove(event);
	}
}
