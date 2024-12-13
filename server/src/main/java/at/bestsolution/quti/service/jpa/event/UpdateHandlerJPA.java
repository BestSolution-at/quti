package at.bestsolution.quti.service.jpa.event;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.dto.EventDTO;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@Singleton
public class UpdateHandlerJPA extends BaseHandler {

	@Inject
	public UpdateHandlerJPA(EntityManager em) {
		super(em);
	}

	public Result<Void> update(UUID calendarKey, UUID eventKey, EventDTO.Patch patch) {
		try {
			return _update(calendarKey, eventKey, patch);
		} catch(WebApplicationException t) {
			return Result.invalidContent(t.getMessage());
		}
	}

	@Transactional
	protected Result<Void> _update(UUID calendarKey, UUID eventKey, EventDTO.Patch patch) {
		Objects.requireNonNull(calendarKey);
		Objects.requireNonNull(eventKey);
		Objects.requireNonNull(patch);

		var em = em();
		var query = em.createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey", EventEntity.class);
		query.setParameter("eventKey", eventKey);
		query.setParameter("calendarKey", calendarKey);

		var result = query.getResultList();
		if( result.size() == 0 ) {
			return Result.notFound("Could not find event '%s' in calendar '%s'", eventKey, calendarKey);
		} else if( result.size() > 1 ) {
			throw new IllegalStateException(String.format("Multiple matching events for '%s' are found.", eventKey));
		}

		var entity = result.get(0);

		EventDTO.Patch.ifTitle(patch, entity::title);
		EventDTO.Patch.ifDescription(patch, entity::description);
		EventDTO.Patch.ifFullday(patch, entity::fullday);
		EventDTO.Patch.ifStart(patch, v -> {
			if( entity.repeatPattern != null ) {
				var newStart = ZonedDateTime.of(entity.start.toLocalDate(), LocalTime.MIN, entity.repeatPattern.recurrenceTimezone);
				// if the repeat date changes we need to clear all modifications
				if( ! newStart.equals(entity.repeatPattern.startDate) ) {
					entity.repeatPattern.startDate = newStart;
					entity.modifications.forEach(em::remove);
				}
			}
			entity.start = v;
		});
		EventDTO.Patch.ifEnd(patch, entity::end);

		var validate = EventUtils.validateEvent(entity);
		if( ! validate.isOk() ) {
			Utils.throwAsException(validate);
		}

		return Result.OK;
	}
}
