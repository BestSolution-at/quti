package at.bestsolution.quti.service.jpa.event;

import java.util.List;
import java.util.UUID;

import org.hibernate.StatelessSession;

import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventModificationEntity;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.BaseHandlerTest;
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

	public List<EventModificationEntity> modifications(UUID eventKey) {
		return session.createQuery("""
			FROM
				EventModification em
			WHERE
				em.event.key = :key
		""", EventModificationEntity.class)
		.setParameter("key", eventKey)
		.getResultList();
	}
}
