package at.bestsolution.qutime.handler.event;

import java.util.UUID;

import org.hibernate.StatelessSession;

import at.bestsolution.qutime.handler.BaseHandler;
import at.bestsolution.qutime.handler.BaseHandlerTest;
import at.bestsolution.qutime.model.EventEntity;
import jakarta.inject.Inject;

public abstract class EventHandlerTest<T extends BaseHandler> extends BaseHandlerTest<T> {
	@Inject
	StatelessSession session;

	public EventHandlerTest(T handler) {
		super(handler);
	}

	public EventEntity event(UUID eventKey) {
		return session
			.createQuery("""
				FROM
					Event e
				JOIN FETCH e.calendar
				OUTER JOIN FETCH e.repeatPattern
				OUTER JOIN FETCH e.references
				WHERE
					e.key = :key
			""", EventEntity.class)
			.setParameter("key", eventKey)
			.getSingleResult();
	}
}
