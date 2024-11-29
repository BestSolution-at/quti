package at.bestsolution.quti.service.jpa.calendar;

import java.util.List;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.model.CalendarEntity;
import at.bestsolution.quti.service.CalendarService;
import at.bestsolution.quti.service.DTOBuilderFactory;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.dto.CalendarDTO;
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

	public Result<CalendarDTO> get(DTOBuilderFactory factory, String key) {
		var parsedKey = Utils.parseUUID(key, "key");
		if (parsedKey.isNotOk()) {
			return parsedKey.toAny();
		}

		var result = getEnties(parsedKey.value());
		if (result.size() == 1) {
			return Result.ok(CalendarDTOUtil.of(result.get(0)));
		} else if (result.size() == 0) {
			return Result.notFound("Could not find calendar with '%s'", key);
		}
		throw new IllegalStateException(String.format("Multiple calendars for '%s' are found", key));
	}
}
