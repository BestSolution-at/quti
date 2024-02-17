package at.bestsolution.qutime.handler.event;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.qutime.Utils.Result;
import at.bestsolution.qutime.dto.EventNewDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO.EventRepeatDailyDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO.EventRepeatRelativeYearlyDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO.EventRepeatWeeklyDTO;
import at.bestsolution.qutime.handler.BaseHandler;
import at.bestsolution.qutime.model.CalendarEntity;
import at.bestsolution.qutime.model.EventEntity;
import at.bestsolution.qutime.model.EventRepeatEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatWeeklyEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CreateHandler extends BaseHandler {

	@Inject
	public CreateHandler(EntityManager em) {
		super(em);
	}

	private CalendarEntity calendar(UUID calendarKey) {
		return em().createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class)
			.setParameter("key", calendarKey)
			.getSingleResult();
	}

	private static <T extends EventRepeatEntity> T fillDefaults(T repeatEntity, EventNewDTO event) {
		var r = event.repeat();
		var recurrenceTimezone = ZoneId.of(r.timeZone);
		repeatEntity.interval = r.interval;
		repeatEntity.startDate = ZonedDateTime.of(event.start().toLocalDate(), LocalTime.MIN, recurrenceTimezone);
		repeatEntity.endDate = r.endDate == null ? null : ZonedDateTime.of(r.endDate, LocalTime.MAX, recurrenceTimezone);
		repeatEntity.recurrenceTimezone = recurrenceTimezone;
		return repeatEntity;
	}

	@Transactional
	public Result<String> create(UUID calendarKey, EventNewDTO event) {
		var eventEntity = new EventEntity();
		eventEntity.calendar = calendar(calendarKey);
		eventEntity.key = UUID.randomUUID();
		eventEntity.title = event.title();
		eventEntity.description = event.description();
		eventEntity.start = event.start();
		eventEntity.end = event.end();

		eventEntity.repeatPattern = event.repeat() == null ? null : createRepeatPattern(event, event.repeat());

		var result = EventUtils.validateEvent(eventEntity);
		if( ! result.isOk() ) {
			return result.toAny();
		}

		if( eventEntity.repeatPattern != null ) {
			em().persist(eventEntity.repeatPattern);
		}
		em().persist(eventEntity);
		return Result.ok(eventEntity.key.toString());
	}

	private static EventRepeatEntity createRepeatPattern(EventNewDTO event, EventRepeatDTO repeat) {
		if( repeat instanceof EventRepeatDailyDTO r ) {
			return fillDefaults(new EventRepeatDailyEntity(), event);
		} else if( repeat instanceof EventRepeatWeeklyDTO r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatWeeklyEntity(), event);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek;
			return repeatPatternEntity;
		} else if( repeat instanceof EventRepeatAbsoluteMonthlyDTO r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatAbsoluteMonthlyEntity(), event);
			repeatPatternEntity.dayOfMonth = r.dayOfMonth;
			return repeatPatternEntity;
		} else if( repeat instanceof EventRepeatAbsoluteYearlyDTO r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatAbsoluteYearlyEntity(), event);
			repeatPatternEntity.dayOfMonth = r.dayOfMonth;
			repeatPatternEntity.month = r.month;
			return repeatPatternEntity;
		} else if( repeat instanceof EventRepeatRelativeMonthlyDTO r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatRelativeMonthlyEntity(), event);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek;
			return repeatPatternEntity;
		} else if( repeat instanceof EventRepeatRelativeYearlyDTO r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatRelativeYearlyEntity(), event);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek;
			repeatPatternEntity.month = r.month;
			return repeatPatternEntity;
		}

		throw new IllegalStateException(String.format("Unknown repeat type %s",repeat.getClass()));
	}
}
