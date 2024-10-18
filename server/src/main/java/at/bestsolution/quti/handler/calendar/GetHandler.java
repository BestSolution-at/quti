package at.bestsolution.quti.handler.calendar;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.dto.CalendarDTO;
import at.bestsolution.quti.dto.CalendarDTOUtil;
import at.bestsolution.quti.handler.BaseReadonlyHandler;
import at.bestsolution.quti.model.CalendarEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class GetHandler extends BaseReadonlyHandler {

	@Inject
	public GetHandler(EntityManager em) {
		super(em);
	}

	protected List<CalendarEntity> getEnties(UUID key) {
		var query = em().createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class);
		query.setParameter("key", key);
		return query.getResultList();
	}

	public CalendarDTO get(UUID key) {
		Objects.requireNonNull(key, "key must not be null");
		var result = getEnties(key);
		if (result.size() == 1) {
			return CalendarDTOUtil.of(result.get(0));
		} else if (result.size() == 0) {
			return null;
		}
		throw new IllegalStateException(String.format("Multiple calendars for '%s' are found", key));
	}
}
