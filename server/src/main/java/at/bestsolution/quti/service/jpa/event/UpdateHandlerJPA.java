package at.bestsolution.quti.service.jpa.event;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatWeeklyEntity;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.dto.EventDTO;
import at.bestsolution.quti.service.dto.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO;
import at.bestsolution.quti.service.dto.EventRepeatDailyDTO;
import at.bestsolution.quti.service.dto.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatRelativeYearlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatWeeklyDTO;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.event.utils.EventRepeatDTOUtil;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import at.bestsolution.quti.service.jpa.event.utils.RepeatDailyUpdater;
import at.bestsolution.quti.service.jpa.event.utils.RepeatWeeklyUpdater;
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
		} catch (WebApplicationException t) {
			return Result.invalidContent(t.getMessage());
		}
	}

	@Transactional
	protected Result<Void> _update(UUID calendarKey, UUID eventKey, EventDTO.Patch patch) {
		Objects.requireNonNull(calendarKey);
		Objects.requireNonNull(eventKey);
		Objects.requireNonNull(patch);

		var em = em();
		var query = em.createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey",
				EventEntity.class);
		query.setParameter("eventKey", eventKey);
		query.setParameter("calendarKey", calendarKey);

		var result = query.getResultList();
		if (result.size() == 0) {
			return Result.notFound("Could not find event '%s' in calendar '%s'", eventKey, calendarKey);
		} else if (result.size() > 1) {
			throw new IllegalStateException(String.format("Multiple matching events for '%s' are found.", eventKey));
		}

		var entity = result.get(0);

		EventDTO.Patch.ifTitle(patch, entity::title);
		EventDTO.Patch.ifDescription(patch, entity::description);
		EventDTO.Patch.ifFullday(patch, entity::fullday);
		EventDTO.Patch.ifStart(patch, v -> {
			if (entity.repeatPattern != null) {
				var newStart = ZonedDateTime.of(entity.start.toLocalDate(), LocalTime.MIN,
						entity.repeatPattern.recurrenceTimezone);
				var current = entity.repeatPattern.startDate.withZoneSameInstant(
						entity.repeatPattern.recurrenceTimezone);
				// if the repeat date changes we need to clear all modifications
				if (!newStart.equals(current)) {
					entity.repeatPattern.startDate = newStart;
					entity.modifications.forEach(em::remove);
				}
			}
			entity.start = v;
		});
		EventDTO.Patch.ifEnd(patch, entity::end);
		EventDTO.Patch.ifRepeat(patch, e -> {
			if (e == null) {
				if (entity.repeatPattern != null) {
					em.remove(entity.repeatPattern);
				}
				entity.repeatPattern = null;
				entity.modifications.forEach(em::remove);
			} else {
				var rv = e.apply(p -> handleUpdateRepeat(em, entity, p), d -> handleNewRepeat(em, entity, d));
				if (!rv.isOk()) {
					Utils.throwAsException(rv);
				}
				var pattern = rv.value();
				em.persist(pattern);
				entity.repeatPattern = pattern;
			}
		});

		var validate = EventUtils.validateEvent(entity);
		if (!validate.isOk()) {
			Utils.throwAsException(validate);
		}

		return Result.OK;
	}

	private static Result<EventRepeatEntity> handleNewRepeat(EntityManager em, EventEntity entity, EventRepeatDTO dto) {
		if (entity.repeatPattern != null) {
			em.remove(entity.repeatPattern);
		}
		entity.modifications.forEach(em::remove);
		return EventRepeatDTOUtil.createRepeatPattern(entity.start.toLocalDate(), dto);
	}

	private static Result<EventRepeatEntity> handleUpdateRepeat(EntityManager em, EventEntity entity,
			EventRepeatDTO.Patch patch) {
		var repeatPattern = entity.repeatPattern;
		if (repeatPattern == null) {
			return Result.invalidContent("No pattern to update");
		}

		if (patch instanceof EventRepeatDailyDTO.Patch p) {
			return RepeatDailyUpdater.handleUpdateRepeatDaily(em, entity, repeatPattern, p);
		} else if (patch instanceof EventRepeatWeeklyDTO.Patch p) {
			return RepeatWeeklyUpdater.handleUpdateRepeatWeekly(em, entity, repeatPattern, p);
		} else if (patch instanceof EventRepeatAbsoluteMonthlyDTO.Patch p) {
			return handleUpdateRepeatAbsoluteMonthly(repeatPattern, p);
		} else if (patch instanceof EventRepeatAbsoluteYearlyDTO.Patch p) {
			return handleUpdateRepeatAbsoluteYearly(repeatPattern, p);
		} else if (patch instanceof EventRepeatRelativeMonthlyDTO.Patch p) {
			return handleUpdateRepeatRelativeMonthly(repeatPattern, p);
		} else if (patch instanceof EventRepeatRelativeYearlyDTO.Patch p) {
			return handleUpdateRepeatRelativeYearly(repeatPattern, p);
		}
		throw new IllegalStateException("Unknown patch type %s".formatted(patch));
	}

	private static Result<EventRepeatEntity> handleUpdateRepeatAbsoluteMonthly(EventRepeatEntity repeatPattern,
			EventRepeatAbsoluteMonthlyDTO.Patch p) {
		if (repeatPattern instanceof EventRepeatAbsoluteMonthlyEntity) {

		}

		return Result.invalidContent("The current repeat-pattern is not absolute monthly");
	}

	private static Result<EventRepeatEntity> handleUpdateRepeatAbsoluteYearly(EventRepeatEntity repeatPattern,
			EventRepeatAbsoluteYearlyDTO.Patch p) {
		if (repeatPattern instanceof EventRepeatAbsoluteYearlyEntity) {

		}

		return Result.invalidContent("The current repeat-pattern is not absolute yearly");
	}

	private static Result<EventRepeatEntity> handleUpdateRepeatRelativeMonthly(EventRepeatEntity repeatPattern,
			EventRepeatRelativeMonthlyDTO.Patch p) {
		if (repeatPattern instanceof EventRepeatRelativeMonthlyEntity) {

		}

		return Result.invalidContent("The current repeat-pattern is not relative monthly");
	}

	private static Result<EventRepeatEntity> handleUpdateRepeatRelativeYearly(EventRepeatEntity repeatPattern,
			EventRepeatRelativeYearlyDTO.Patch p) {
		if (repeatPattern instanceof EventRepeatRelativeYearlyEntity) {

		}

		return Result.invalidContent("The current repeat-pattern is not relative yearly");
	}
}
