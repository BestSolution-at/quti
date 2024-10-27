package at.bestsolution.quti.service.jpa.event;

import java.time.ZoneId;
import java.time.ZoneOffset;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.dto.EventDTO;
import at.bestsolution.quti.model.EventEntity;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseReadonlyHandler;
import at.bestsolution.quti.service.jpa.event.utils.EventDTOUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class GetHandlerJPA extends BaseReadonlyHandler implements EventService.GetHandler {

	@Inject
	public GetHandlerJPA(EntityManager em) {
		super(em);
	}

	public Result<EventDTO> get(String calendarKey, String eventKey, ZoneId zone) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

		if( parsedCalendarKey.isNotOk() ) {
			return parsedCalendarKey.toAny();
		}
		if( parsedEventKey.isNotOk() ) {
			return parsedEventKey.toAny();
		}

		var query = em().createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey",
				EventEntity.class);
		query.setParameter("eventKey", parsedEventKey.value());
		query.setParameter("calendarKey", parsedCalendarKey.value());
		var result = query.getResultList();
		if (result.size() == 1) {
			return Result.ok(EventDTOUtil.of(result.get(0), zone == null ? ZoneOffset.UTC : zone));
		} else if (result.size() == 0) {
			return Result.notFound("Could not find event with '%s' in calendar '%s'", eventKey, calendarKey);
		}
		throw new IllegalStateException(String.format("Multiple matching events for '%s' are found.", eventKey));
	}
}
