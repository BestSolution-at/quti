package at.bestsolution.qutime.handler.calendar;

import java.util.UUID;

import org.hibernate.StatelessSession;

import at.bestsolution.qutime.handler.BaseHandler;
import at.bestsolution.qutime.handler.BaseHandlerTest;
import at.bestsolution.qutime.model.CalendarEntity;
import jakarta.inject.Inject;

public class CalendarHandlerTest<T extends BaseHandler> extends BaseHandlerTest<T> {
	@Inject
	StatelessSession session;

	public CalendarHandlerTest(T handler) {
		super(handler);
	}

	public CalendarEntity calendar(UUID key) {
		return session
				.createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class)
				.setParameter("key", key)
				.getSingleResult();
	}
}
