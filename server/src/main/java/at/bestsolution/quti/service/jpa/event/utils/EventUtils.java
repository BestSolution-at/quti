package at.bestsolution.quti.service.jpa.event.utils;

import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.Result;
import jakarta.persistence.EntityManager;

import java.util.UUID;

public class EventUtils {
	public static Result<Void> validateEvent(EventEntity entity) {
		if( entity.start == null ) {
			return Result.invalidContent("event.start must not be null");
		}
		if( entity.end == null ) {
			return Result.invalidContent("event.end must not be null");
		}
		if( entity.title == null ) {
			return Result.invalidContent("event.title must not be null");
		}
		if( entity.title.isBlank() ) {
			return Result.invalidContent("event.title must not be blank");
		}
		if( entity.start.isAfter(entity.end) || entity.start.equals(entity.end) ) {
			return Result.invalidContent("event.start has to be before event.end");
		}
		return Result.OK;
	}

	public static EventEntity event(EntityManager em, UUID calendarKey, UUID eventKey) {
		var result = em.createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey",
				EventEntity.class)
				.setParameter("eventKey", eventKey)
				.setParameter("calendarKey", calendarKey)
				.getResultList();

			if( result.size() == 1 ) {
				return result.get(0);
			} else if( result.size() == 0 ) {
				return null;
			}
			throw new IllegalStateException("Multiple events with key '%s' found");

	}
}
