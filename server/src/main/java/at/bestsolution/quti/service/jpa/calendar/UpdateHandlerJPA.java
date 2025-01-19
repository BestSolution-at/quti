package at.bestsolution.quti.service.jpa.calendar;

import at.bestsolution.quti.service.model.Calendar;
import at.bestsolution.quti.service.CalendarService;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class UpdateHandlerJPA extends BaseHandler implements CalendarService.UpdateHandler {

	@Inject
	public UpdateHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> update(BuilderFactory factory, String key, Calendar.Patch patch) {
		/*
		 * Objects.requireNonNull(key, "key must not be null");
		 * Objects.requireNonNull(patch, "patch must not be null");
		 *
		 * var parsedKey = Utils.parseUUID(key, "key");
		 *
		 * var query = em().createQuery("FROM Calendar WHERE key = :key",
		 * CalendarEntity.class);
		 * query.setParameter("key", parsedKey.value());
		 *
		 * try {
		 * var entity = query.getSingleResult();
		 *
		 * Calendar.Patch.ifName(patch, entity::name);
		 *
		 * if (patch.owner() != null && entity.owner != null) {
		 * return Result.invalidContent("Calendar has already an owner set");
		 * }
		 *
		 * Calendar.Patch.ifOwner(patch, entity::owner);
		 *
		 * return Result.OK;
		 * } catch (NoResultException e) {
		 * return Result.notFound("Could not find calendar with '%s'", key);
		 * }
		 */
		return Result.OK;
	}
}
