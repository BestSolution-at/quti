package at.bestsolution.quti.calendar.service.jpa.event;

import java.time.ZoneId;
import java.time.ZoneOffset;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.NotFoundException;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.impl.EventServiceImpl;
import at.bestsolution.quti.calendar.service.jpa.BaseReadonlyHandler;
import at.bestsolution.quti.calendar.service.jpa.event.utils.EventDTOUtil;
import at.bestsolution.quti.calendar.service.jpa.model.EventEntity;
import at.bestsolution.quti.calendar.service.model.Event;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class GetHandlerJPA extends BaseReadonlyHandler implements EventServiceImpl.GetHandler {

	@Inject
	public GetHandlerJPA(EntityManager em) {
		super(em);
	}

	public Event.Data get(BuilderFactory factory, String calendarKey, String eventKey, ZoneId zone) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

		var query = em().createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey",
				EventEntity.class);
		query.setParameter("eventKey", parsedEventKey);
		query.setParameter("calendarKey", parsedCalendarKey);
		var result = query.getResultList();
		if (result.size() == 1) {
			return EventDTOUtil.of(factory, result.get(0), zone == null ? ZoneOffset.UTC : zone);
		} else if (result.size() == 0) {
			throw new NotFoundException("Could not find event with '%s' in calendar '%s'".formatted(eventKey, calendarKey));
		}
		throw new IllegalStateException(String.format("Multiple matching events for '%s' are found.", eventKey));
	}
}
