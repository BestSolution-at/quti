package at.bestsolution.quti.calendar.service.jpa.event;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.NotFoundException;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.impl.EventServiceImpl;
import at.bestsolution.quti.calendar.service.jpa.BaseHandler;
import at.bestsolution.quti.calendar.service.jpa.event.utils.EventUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class DeleteHandlerJPA extends BaseHandler implements EventServiceImpl.DeleteHandler {

	@Inject
	public DeleteHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void delete(BuilderFactory factory, String calendarKey, String eventKey) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

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
