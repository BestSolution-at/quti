package at.bestsolution.quti.calendar.service.jpa.calendar;

import java.util.Objects;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.impl.CalendarServiceImpl;
import at.bestsolution.quti.calendar.service.jpa.BaseHandler;
import at.bestsolution.quti.calendar.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.calendar.service.model.Calendar;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@Singleton
public class UpdateHandlerJPA extends BaseHandler implements CalendarServiceImpl.UpdateHandler {

	@Inject
	public UpdateHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void update(BuilderFactory factory, String key, Calendar.Patch patch) {
		Objects.requireNonNull(key, "key must not be null");
		Objects.requireNonNull(patch, "patch must not be null");

		var parsedKey = Utils.parseUUID(key, "key");
		var query = em().createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class);
		query.setParameter("key", parsedKey);
		try {
			var entity = query.getSingleResult();
			patch.name().ifPresent(entity::name);
			patch.owner().accept(entity::owner);
		} catch (NoResultException e) {
			throw new NotFoundException("Could not find calendar with '%s'".formatted(key));
		}
	}
}
