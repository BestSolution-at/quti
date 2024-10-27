package at.bestsolution.quti.service.jpa.calendar;

import java.util.ArrayList;
import java.util.Objects;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.model.CalendarEntity;
import at.bestsolution.quti.service.CalendarService;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.json.JsonValue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Singleton
public class UpdateHandlerImpl extends BaseHandler implements CalendarService.UpdateHandler {

	@Inject
	public UpdateHandlerImpl(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> update(String key, String patch) {
		Objects.requireNonNull(key, "key must not be null");
		Objects.requireNonNull(patch, "patch must not be null");

		var parsedKey = Utils.parseUUID(key, "key");
		var parsedPatch = Utils.parseJsonPatch(patch, "patch");

		if (parsedKey.isNotOk()) {
			return parsedKey.toAny();
		}
		if (parsedPatch.isNotOk()) {
			return parsedPatch.toAny();
		}

		var query = em().createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class);
		query.setParameter("key", parsedKey.value());

		try {
			var entity = query.getSingleResult();
			var updateRunnables = new ArrayList<Runnable>();
			for (JsonValue e : parsedPatch.value().toJsonArray()) {
				var op = e.asJsonObject();
				var operation = op.getString("op");
				var path = op.getString("path");

				if ("add".equals(operation)) {
					if ("owner".equals(path)) {
						if (entity.owner == null) {
							updateRunnables.add(() -> entity.owner = op.getString("value"));
						} else {
							return Result.invalidContent("Calendar has already an owner set");
						}
					} else {
						return Result.invalidContent("Operation '%s' on '%s' is not allowed", operation, path);
					}
				} else if ("replace".equals(operation)) {
					if ("name".equals(path)) {
						updateRunnables.add(() -> entity.name = op.getString("value"));
					} else {
						return Result.invalidContent("Updating '%s' is not allowed", path);
					}
				} else {
					return Result.invalidContent("Operation '%s' on '%s' is not allowed", operation, path);
				}
			}

			updateRunnables.forEach(Runnable::run);
			return Result.OK;
		} catch (NoResultException e) {
			return Result.notFound("Could not find calendar with '%s'", key);
		}
	}
}
