package at.bestsolution.quti.service.jpa.calendar;

import java.util.List;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.service.model.Calendar;
import at.bestsolution.quti.service.CalendarService;
import at.bestsolution.quti.service.NotFoundException;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.jpa.BaseReadonlyHandler;
import at.bestsolution.quti.service.jpa.calendar.utils.CalendarDTOUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class GetHandlerJPA extends BaseReadonlyHandler implements CalendarService.GetHandler {

	@Inject
	public GetHandlerJPA(EntityManager em) {
		super(em);
	}

	protected List<CalendarEntity> getEnties(UUID key) {
		var query = em().createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class);
		query.setParameter("key", key);
		return query.getResultList();
	}

	public Calendar.Data get(BuilderFactory factory, String key) {
		var parsedKey = Utils._parseUUID(key, "key");

		var result = getEnties(parsedKey);
		if (result.size() == 1) {
			return CalendarDTOUtil.of(factory, result.get(0));
		} else if (result.size() == 0) {
			throw new NotFoundException("Could not find calendar with '%s'".formatted(key));
		}
		throw new IllegalStateException(String.format("Multiple calendars for '%s' are found", key));
	}
}
