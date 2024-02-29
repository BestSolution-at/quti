package at.bestsolution.quti.handler.calendar;

import java.util.UUID;

import org.hibernate.StatelessSession;

import at.bestsolution.quti.handler.BaseHandler;
import at.bestsolution.quti.handler.BaseHandlerTest;
import at.bestsolution.quti.model.CalendarEntity;
import jakarta.inject.Inject;

public abstract class CalendarHandlerTest<T extends BaseHandler> extends BaseHandlerTest<T> {
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
