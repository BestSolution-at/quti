package at.bestsolution.quti.calendar.service.jpa.calendar;

import java.time.ZonedDateTime;
import java.util.Objects;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.InvalidArgumentException;
import at.bestsolution.quti.calendar.service.NotFoundException;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.impl.CalendarServiceImpl;
import at.bestsolution.quti.calendar.service.jpa.BaseHandler;
import at.bestsolution.quti.calendar.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.calendar.service.jpa.model.EventEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CloseHandlerJPA extends BaseHandler implements CalendarServiceImpl.CloseHandler {

	@Inject
	public CloseHandlerJPA(EntityManager em) {
		super(em);
	}

	@Override
	@Transactional
	public void close(BuilderFactory _factory, String key, ZonedDateTime date)
			throws NotFoundException, InvalidArgumentException {
		Objects.requireNonNull(key, "key must not be null");
		Objects.requireNonNull(date, "date must not be null");

		var parsedKey = Utils.parseUUID(key, "key");
		var em = em();
		var query = em.createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class);
		query.setParameter("key", parsedKey);
		try {
			var entity = query.getSingleResult();
			removeAllFutureEvents(em, entity, date);
			endAllRecurringEvents(em, entity, date);
		} catch (jakarta.persistence.NoResultException e) {
			throw new NotFoundException("Could not find calendar with '%s'".formatted(key));
		}
	}

	private static void removeAllFutureEvents(EntityManager em, CalendarEntity entity, ZonedDateTime date) {
		var query = em.createQuery("FROM Event WHERE calendar = :calendar AND start >= :date", EventEntity.class);
		query.setParameter("calendar", entity);
		query.setParameter("date", date);
		query.getResultList().forEach(e -> {
			e.modifications.forEach(em::remove);
			e.references.forEach(em::remove);
			if (e.repeatPattern != null) {
				em.remove(e.repeatPattern);
			}
			em.remove(e);
		});
	}

	private static void endAllRecurringEvents(EntityManager em, CalendarEntity entity, ZonedDateTime date) {
		var query = em.createQuery("""
				FROM
					Event
				WHERE
					calendar = :calendar
				AND repeatPattern IS NOT NULL
				AND (repeatPattern.endDate IS NULL OR repeatPattern.endDate > :date)""",
				EventEntity.class);
		query.setParameter("calendar", entity);
		query.setParameter("date", date);
		query.getResultList().forEach(e -> {
			if (e.repeatPattern != null) {
				e.repeatPattern.endDate = date;
				em.persist(e.repeatPattern);
			}
		});
	}
}
