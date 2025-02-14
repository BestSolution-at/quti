package at.bestsolution.quti.service.jpa.event.utils;

import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.InvalidContentException;
import jakarta.persistence.EntityManager;

import java.util.UUID;

public class EventUtils {
	public static void validateEvent(EventEntity entity) {
		if (entity.start == null) {
			throw new InvalidContentException("event.start must not be null");
		}
		if (entity.end == null) {
			throw new InvalidContentException("event.end must not be null");
		}
		if (entity.title == null) {
			throw new InvalidContentException("event.title must not be null");
		}
		if (entity.title.isBlank()) {
			throw new InvalidContentException("event.title must not be blank");
		}
		if (entity.start.isAfter(entity.end) || entity.start.equals(entity.end)) {
			throw new InvalidContentException("event.start has to be before event.end");
		}
	}

	public static EventEntity event(EntityManager em, UUID calendarKey, UUID eventKey) {
		var result = em.createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey",
				EventEntity.class)
				.setParameter("eventKey", eventKey)
				.setParameter("calendarKey", calendarKey)
				.getResultList();

		if (result.size() == 1) {
			return result.get(0);
		} else if (result.size() == 0) {
			return null;
		}
		throw new IllegalStateException("Multiple events with key '%s' found");

	}
}
