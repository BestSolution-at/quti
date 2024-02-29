package at.bestsolution.quti.handler.event;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.Utils.Result;
import at.bestsolution.quti.dto.EventNewDTO;
import at.bestsolution.quti.dto.EventRepeatDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatDailyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatRelativeYearlyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatWeeklyDTO;
import at.bestsolution.quti.handler.BaseHandler;
import at.bestsolution.quti.handler.calendar.CalendarUtils;
import at.bestsolution.quti.model.CalendarEntity;
import at.bestsolution.quti.model.EventEntity;
import at.bestsolution.quti.model.EventReferenceEntity;
import at.bestsolution.quti.model.EventRepeatEntity;
import at.bestsolution.quti.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatWeeklyEntity;
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
		repeatEntity.endDate = r.endDate == null ? null : Utils.atEndOfDay(ZonedDateTime.of(r.endDate, LocalTime.NOON, recurrenceTimezone));
		repeatEntity.recurrenceTimezone = recurrenceTimezone;
		return repeatEntity;
	}

	@Transactional
	public Result<String> create(UUID calendarKey, EventNewDTO event) {
		var em = em();
		var eventEntity = new EventEntity();
		eventEntity.calendar = calendar(calendarKey);
		eventEntity.key = UUID.randomUUID();
		eventEntity.title = event.title();
		eventEntity.description = event.description();
		eventEntity.start = event.fullday() ? Utils.atStartOfDay(event.start()) : event.start();
		eventEntity.end = event.fullday() ? Utils.atEndOfDay(event.end()) : event.end();
		eventEntity.fullday = event.fullday();
		eventEntity.tags = event.tags();

		eventEntity.repeatPattern = event.repeat() == null ? null : createRepeatPattern(event, event.repeat());

		List<EventReferenceEntity> references = List.of();
		if( event.referencedCalendars() != null && event.referencedCalendars().size() > 0 ) {
			references = event.referencedCalendars().stream()
				.map(UUID::fromString)
				.map(key -> CalendarUtils.calendar(em, key))
				.filter(Objects::nonNull)
				.map( calendar -> {
					var ref = new EventReferenceEntity();
					ref.calendar = calendar;
					ref.event = eventEntity;
					return ref;
				}).toList();

			if( references.size() != event.referencedCalendars().size() ) {
				return Result.invalidContent("At least one calendar reference could not be resolved");
			}
		}

		var result = EventUtils.validateEvent(eventEntity);
		if( ! result.isOk() ) {
			return result.toAny();
		}

		if( eventEntity.repeatPattern != null ) {
			em.persist(eventEntity.repeatPattern);
		}
		em.persist(eventEntity);

		references.forEach(em::persist);

		return Result.ok(eventEntity.key.toString());
	}

	private static EventRepeatEntity createRepeatPattern(EventNewDTO event, EventRepeatDTO repeat) {
		if( repeat instanceof EventRepeatDailyDTO ) {
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
