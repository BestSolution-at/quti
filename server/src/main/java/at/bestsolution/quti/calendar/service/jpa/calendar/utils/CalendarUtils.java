package at.bestsolution.quti.calendar.service.jpa.calendar.utils;

import java.util.UUID;

import at.bestsolution.quti.calendar.service.jpa.model.CalendarEntity;
import jakarta.persistence.EntityManager;

public class CalendarUtils {
	public static CalendarEntity calendar(EntityManager em, UUID key) {
		var result = em.createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class)
				.setParameter("key", key)
				.getResultList();
		if (result.size() == 1) {
			return result.get(0);
		} else if (result.size() == 0) {
			return null;
		}
		throw new IllegalStateException("Multiple calendars with key '%s' found");
	}
}
