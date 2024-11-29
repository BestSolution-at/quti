package at.bestsolution.quti.service.jpa.event;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.Utils;
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
import at.bestsolution.quti.rest.dto.EventNewDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatAbsoluteMonthlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatAbsoluteYearlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatDailyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatRelativeMonthlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatRelativeYearlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatWeeklyDTOImpl;
import at.bestsolution.quti.service.DTOBuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.calendar.utils.CalendarUtils;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CreateHandlerJPA extends BaseHandler implements EventService.CreateHandler {

	@Inject
	public CreateHandlerJPA(EntityManager em) {
		super(em);
	}

	private CalendarEntity calendar(UUID calendarKey) {
		return em().createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class)
			.setParameter("key", calendarKey)
			.getSingleResult();
	}

	private static <T extends EventRepeatEntity> T fillDefaults(T repeatEntity, EventNewDTOImpl event) {
		var r = event.repeat();
		var recurrenceTimezone = r.timeZone;
		repeatEntity.interval = r.interval;
		repeatEntity.startDate = ZonedDateTime.of(event.start().toLocalDate(), LocalTime.MIN, recurrenceTimezone);
		repeatEntity.endDate = r.endDate == null ? null : Utils.atEndOfDay(ZonedDateTime.of(r.endDate, LocalTime.NOON, recurrenceTimezone));
		repeatEntity.recurrenceTimezone = recurrenceTimezone;
		return repeatEntity;
	}

	@Transactional
	public Result<String> create(DTOBuilderFactory factory, String calendarKey, EventNewDTOImpl event) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		if( parsedCalendarKey.isNotOk() ) {
			return parsedCalendarKey.toAny();
		}
		var em = em();
		var eventEntity = new EventEntity();
		eventEntity.calendar = calendar(parsedCalendarKey.value());
		eventEntity.key = UUID.randomUUID();
		eventEntity.title = event.title();
		eventEntity.description = event.description();
		eventEntity.start = event.fullday() ? Utils.atStartOfDay(event.start()) : event.start();
		eventEntity.end = event.fullday() ? Utils.atEndOfDay(event.end()) : event.end();
		eventEntity.fullday = event.fullday();
		eventEntity.tags = event.tags();

		if( event.repeat() != null ) {
			var rv = createRepeatPattern(event, event.repeat());
			if( ! rv.isOk() ) {
				return rv.toAny();
			}

			eventEntity.repeatPattern = rv.value();
		}

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

	private static Result<EventRepeatEntity> createRepeatPattern(EventNewDTOImpl event, EventRepeatDTOImpl repeat) {
		if( repeat.interval <= 0 ) {
			return Result.invalidContent("Interval must be greater than 0");
		}
		if( repeat instanceof EventRepeatDailyDTOImpl ) {
			return Result.ok(fillDefaults(new EventRepeatDailyEntity(), event));
		} else if( repeat instanceof EventRepeatWeeklyDTOImpl r ) {
			if( r.daysOfWeek.isEmpty() ) {
				return Result.invalidContent("Weekdays must not be empty");
			}
			var repeatPatternEntity = fillDefaults(new EventRepeatWeeklyEntity(), event);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek;
			return Result.ok(repeatPatternEntity);
		} else if( repeat instanceof EventRepeatAbsoluteMonthlyDTOImpl r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatAbsoluteMonthlyEntity(), event);
			repeatPatternEntity.dayOfMonth = r.dayOfMonth;
			return Result.ok(repeatPatternEntity);
		} else if( repeat instanceof EventRepeatAbsoluteYearlyDTOImpl r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatAbsoluteYearlyEntity(), event);
			repeatPatternEntity.dayOfMonth = r.dayOfMonth;
			repeatPatternEntity.month = r.month;
			return Result.ok(repeatPatternEntity);
		} else if( repeat instanceof EventRepeatRelativeMonthlyDTOImpl r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatRelativeMonthlyEntity(), event);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek;
			return Result.ok(repeatPatternEntity);
		} else if( repeat instanceof EventRepeatRelativeYearlyDTOImpl r ) {
			var repeatPatternEntity = fillDefaults(new EventRepeatRelativeYearlyEntity(), event);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek;
			repeatPatternEntity.month = r.month;
			return Result.ok(repeatPatternEntity);
		}

		throw new IllegalStateException(String.format("Unknown repeat type %s",repeat.getClass()));
	}
}
