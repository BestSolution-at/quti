package at.bestsolution.quti.service.jpa.event;

import java.util.UUID;

import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.jpa.BaseHandler;
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

	public Result<Void> update(UUID calendarKey, UUID eventKey, Event.Patch patch) {
		try {
			return _update(calendarKey, eventKey, patch);
		} catch (WebApplicationException t) {
			return Result.invalidContent(t.getMessage());
		}
	}

	@Transactional
	protected Result<Void> _update(UUID calendarKey, UUID eventKey, Event.Patch patch) {
		return Result.OK;
		/*
		 * Objects.requireNonNull(calendarKey);
		 * Objects.requireNonNull(eventKey);
		 * Objects.requireNonNull(patch);
		 *
		 * var em = em();
		 * var query = em.
		 * createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey"
		 * ,
		 * EventEntity.class);
		 * query.setParameter("eventKey", eventKey);
		 * query.setParameter("calendarKey", calendarKey);
		 *
		 * var result = query.getResultList();
		 * if (result.size() == 0) {
		 * return Result.notFound("Could not find event '%s' in calendar '%s'",
		 * eventKey, calendarKey);
		 * } else if (result.size() > 1) {
		 * throw new IllegalStateException(String.
		 * format("Multiple matching events for '%s' are found.", eventKey));
		 * }
		 *
		 * var entity = result.get(0);
		 *
		 * Event.Patch.ifTitle(patch, entity::title);
		 * Event.Patch.ifDescription(patch, entity::description);
		 * Event.Patch.ifFullday(patch, entity::fullday);
		 * Event.Patch.ifStart(patch, v -> {
		 * if (entity.repeatPattern != null) {
		 * var newStart = ZonedDateTime.of(entity.start.toLocalDate(), LocalTime.MIN,
		 * entity.repeatPattern.recurrenceTimezone);
		 * var current = entity.repeatPattern.startDate.withZoneSameInstant(
		 * entity.repeatPattern.recurrenceTimezone);
		 * // if the repeat date changes we need to clear all modifications
		 * if (!newStart.equals(current)) {
		 * entity.repeatPattern.startDate = newStart;
		 * entity.modifications.forEach(em::remove);
		 * }
		 * }
		 * entity.start = v;
		 * });
		 * Event.Patch.ifEnd(patch, entity::end);
		 * Event.Patch.ifRepeat(patch, e -> {
		 * if (e == null) {
		 * if (entity.repeatPattern != null) {
		 * em.remove(entity.repeatPattern);
		 * }
		 * entity.repeatPattern = null;
		 * entity.modifications.forEach(em::remove);
		 * } else {
		 * var rv = e.apply(p -> handleUpdateRepeat(em, entity, p), d ->
		 * handleNewRepeat(em, entity, d));
		 * if (!rv.isOk()) {
		 * Utils.throwAsException(rv);
		 * }
		 * var pattern = rv.value();
		 * em.persist(pattern);
		 * entity.repeatPattern = pattern;
		 * }
		 * });
		 *
		 * var validate = EventUtils.validateEvent(entity);
		 * if (!validate.isOk()) {
		 * Utils.throwAsException(validate);
		 * }
		 *
		 * return Result.OK;
		 */
	}

	/*
	 * private static Result<EventRepeatEntity> handleNewRepeat(EntityManager em,
	 * EventEntity entity, EventRepeat.Data dto) {
	 * if (entity.repeatPattern != null) {
	 * em.remove(entity.repeatPattern);
	 * }
	 * entity.modifications.forEach(em::remove);
	 * return EventRepeatDTOUtil.createRepeatPattern(entity.start.toLocalDate(),
	 * dto);
	 * }
	 *
	 * private static Result<EventRepeatEntity> handleUpdateRepeat(EntityManager em,
	 * EventEntity entity,
	 * EventRepeat.Patch patch) {
	 * var repeatPattern = entity.repeatPattern;
	 * if (repeatPattern == null) {
	 * return Result.invalidContent("No pattern to update");
	 * }
	 *
	 * if (patch instanceof EventRepeatDaily.Patch p) {
	 * return RepeatDailyUpdater.handleUpdateRepeatDaily(em, entity, repeatPattern,
	 * p);
	 * } else if (patch instanceof EventRepeatWeekly.Patch p) {
	 * return RepeatWeeklyUpdater.handleUpdateRepeatWeekly(em, entity,
	 * repeatPattern, p);
	 * } else if (patch instanceof EventRepeatAbsoluteMonthly.Patch p) {
	 * return handleUpdateRepeatAbsoluteMonthly(repeatPattern, p);
	 * } else if (patch instanceof EventRepeatAbsoluteYearly.Patch p) {
	 * return handleUpdateRepeatAbsoluteYearly(repeatPattern, p);
	 * } else if (patch instanceof EventRepeatRelativeMonthly.Patch p) {
	 * return handleUpdateRepeatRelativeMonthly(repeatPattern, p);
	 * } else if (patch instanceof EventRepeatRelativeYearly.Patch p) {
	 * return handleUpdateRepeatRelativeYearly(repeatPattern, p);
	 * }
	 * throw new IllegalStateException("Unknown patch type %s".formatted(patch));
	 * }
	 *
	 * private static Result<EventRepeatEntity>
	 * handleUpdateRepeatAbsoluteMonthly(EventRepeatEntity repeatPattern,
	 * EventRepeatAbsoluteMonthly.Patch p) {
	 * if (repeatPattern instanceof EventRepeatAbsoluteMonthlyEntity) {
	 *
	 * }
	 *
	 * return
	 * Result.invalidContent("The current repeat-pattern is not absolute monthly");
	 * }
	 *
	 * private static Result<EventRepeatEntity>
	 * handleUpdateRepeatAbsoluteYearly(EventRepeatEntity repeatPattern,
	 * EventRepeatAbsoluteYearly.Patch p) {
	 * if (repeatPattern instanceof EventRepeatAbsoluteYearlyEntity) {
	 *
	 * }
	 *
	 * return
	 * Result.invalidContent("The current repeat-pattern is not absolute yearly");
	 * }
	 *
	 * private static Result<EventRepeatEntity>
	 * handleUpdateRepeatRelativeMonthly(EventRepeatEntity repeatPattern,
	 * EventRepeatRelativeMonthly.Patch p) {
	 * if (repeatPattern instanceof EventRepeatRelativeMonthlyEntity) {
	 *
	 * }
	 *
	 * return
	 * Result.invalidContent("The current repeat-pattern is not relative monthly");
	 * }
	 *
	 * private static Result<EventRepeatEntity>
	 * handleUpdateRepeatRelativeYearly(EventRepeatEntity repeatPattern,
	 * EventRepeatRelativeYearly.Patch p) {
	 * if (repeatPattern instanceof EventRepeatRelativeYearlyEntity) {
	 *
	 * }
	 *
	 * return
	 * Result.invalidContent("The current repeat-pattern is not relative yearly");
	 * }
	 */
}
