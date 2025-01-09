package at.bestsolution.quti.service.jpa.event.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatWeeklyEntity;
import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.DTOBuilderFactory;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.dto.EventRepeatDTO;
import at.bestsolution.quti.service.dto.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDailyDTO;
import at.bestsolution.quti.service.dto.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatRelativeYearlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatWeeklyDTO;
import at.bestsolution.quti.service.dto.MixinEventRepeatDataDTO;

public class EventRepeatDTOUtil {
	public static EventRepeatDTO of(DTOBuilderFactory factory, EventRepeatEntity entity) {
		if (entity == null) {
			return null;
		}

		if (entity instanceof EventRepeatAbsoluteMonthlyEntity e) {
			return EventRepeatAbsoluteMonthlyDTOUtil.of(factory, e);
		} else if (entity instanceof EventRepeatAbsoluteYearlyEntity e) {
			return EventRepeatAbsoluteYearlyDTOUtil.of(factory, e);
		} else if (entity instanceof EventRepeatDailyEntity e) {
			return EventRepeatDailyDTOUtil.of(factory, e);
		} else if (entity instanceof EventRepeatRelativeMonthlyEntity e) {
			return EventRepeatRelativeMonthlyDTOUtil.of(factory, e);
		} else if (entity instanceof EventRepeatRelativeYearlyEntity e) {
			return EventRepeatRelativeYearlyDTOUtil.of(factory, e);
		} else if (entity instanceof EventRepeatWeeklyEntity e) {
			return EventRepeatWeeklyDTOUtil.of(factory, e);
		}
		throw new IllegalArgumentException("Unknown entity '" + entity + "'");
	}

	public class EventRepeatDailyDTOUtil {
		public static EventRepeatDailyDTO of(DTOBuilderFactory factory, EventRepeatDailyEntity entity) {
			var b = factory.builder(EventRepeatDailyDTO.Builder.class);
			return b
					.interval(entity.interval)
					.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate()
							: null)
					.timeZone(entity.recurrenceTimezone)
					.build();
		}

		public static EventRepeatDailyDTO of(DTOBuilderFactory factory, short interval, String timeZone,
				LocalDate endDate) {
			var b = factory.builder(EventRepeatDailyDTO.Builder.class);
			return b
					.interval(interval)
					.timeZone(ZoneId.of(timeZone))
					.endDate(endDate)
					.build();
		}
	}

	public class EventRepeatWeeklyDTOUtil {
		public static EventRepeatWeeklyDTO of(DTOBuilderFactory factory, EventRepeatWeeklyEntity entity) {
			var b = factory.builder(EventRepeatWeeklyDTO.Builder.class);
			return b
					.interval(entity.interval)
					.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate()
							: null)
					.timeZone(entity.recurrenceTimezone)
					.daysOfWeek(entity.daysOfWeek)
					.build();
		}

		public static EventRepeatWeeklyDTO of(DTOBuilderFactory factory, short interval, String timeZone, LocalDate endDate,
				List<DayOfWeek> daysOfWeek) {
			var b = factory.builder(EventRepeatWeeklyDTO.Builder.class);
			return b
					.interval(interval)
					.endDate(endDate)
					.timeZone(ZoneId.of(timeZone))
					.daysOfWeek(daysOfWeek)
					.build();
		}
	}

	public class EventRepeatAbsoluteMonthlyDTOUtil {
		public static EventRepeatAbsoluteMonthlyDTO of(DTOBuilderFactory factory, EventRepeatAbsoluteMonthlyEntity entity) {
			var b = factory.builder(EventRepeatAbsoluteMonthlyDTO.Builder.class);
			return b
					.interval(entity.interval)
					.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate()
							: null)
					.timeZone(entity.recurrenceTimezone)
					.dayOfMonth(entity.dayOfMonth)
					.build();
		}
	}

	public class EventRepeatAbsoluteYearlyDTOUtil {
		public static EventRepeatAbsoluteYearlyDTO of(DTOBuilderFactory factory, EventRepeatAbsoluteYearlyEntity entity) {
			var b = factory.builder(EventRepeatAbsoluteYearlyDTO.Builder.class);
			return b
					.interval(entity.interval)
					.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate()
							: null)
					.timeZone(entity.recurrenceTimezone)
					.dayOfMonth(entity.dayOfMonth)
					.month(entity.month)
					.build();
		}
	}

	public class EventRepeatRelativeMonthlyDTOUtil {
		public static EventRepeatRelativeMonthlyDTO of(DTOBuilderFactory factory, EventRepeatRelativeMonthlyEntity entity) {
			var b = factory.builder(EventRepeatRelativeMonthlyDTO.Builder.class);
			return b
					.interval(entity.interval)
					.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate()
							: null)
					.timeZone(entity.recurrenceTimezone)
					.daysOfWeek(entity.daysOfWeek)
					.build();
		}
	}

	public class EventRepeatRelativeYearlyDTOUtil {
		public static EventRepeatRelativeYearlyDTO of(DTOBuilderFactory factory, EventRepeatRelativeYearlyEntity entity) {
			var b = factory.builder(EventRepeatRelativeYearlyDTO.Builder.class);
			return b
					.interval(entity.interval)
					.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate()
							: null)
					.timeZone(entity.recurrenceTimezone)
					.daysOfWeek(entity.daysOfWeek)
					.month(entity.month)
					.build();
		}
	}

	private static <T extends EventRepeatEntity> T fillDefaults(T repeatEntity, LocalDate startDate, EventRepeatDTO r) {
		var recurrenceTimezone = r.timeZone();
		repeatEntity.interval = r.interval();
		repeatEntity.startDate = ZonedDateTime.of(startDate, LocalTime.MIN, recurrenceTimezone);
		repeatEntity.endDate = r.endDate() == null ? null
				: Utils.atEndOfDay(ZonedDateTime.of(r.endDate(), LocalTime.NOON, recurrenceTimezone));
		repeatEntity.recurrenceTimezone = recurrenceTimezone;
		return repeatEntity;
	}

	public static Result<EventRepeatEntity> createRepeatPattern(LocalDate startDate, EventRepeatDTO repeat) {
		if (repeat instanceof MixinEventRepeatDataDTO r) {
			if (r.interval() <= 0) {
				return Result.invalidContent("Interval must be greater than 0");
			}
		}

		if (repeat instanceof EventRepeatDailyDTO) {
			return Result.ok(fillDefaults(new EventRepeatDailyEntity(), startDate, repeat));
		} else if (repeat instanceof EventRepeatWeeklyDTO r) {
			if (r.daysOfWeek().isEmpty()) {
				return Result.invalidContent("Weekdays must not be empty");
			}
			var repeatPatternEntity = fillDefaults(new EventRepeatWeeklyEntity(), startDate, repeat);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek();
			return Result.ok(repeatPatternEntity);
		} else if (repeat instanceof EventRepeatAbsoluteMonthlyDTO r) {
			var repeatPatternEntity = fillDefaults(new EventRepeatAbsoluteMonthlyEntity(), startDate, repeat);
			repeatPatternEntity.dayOfMonth = r.dayOfMonth();
			return Result.ok(repeatPatternEntity);
		} else if (repeat instanceof EventRepeatAbsoluteYearlyDTO r) {
			var repeatPatternEntity = fillDefaults(new EventRepeatAbsoluteYearlyEntity(), startDate, repeat);
			repeatPatternEntity.dayOfMonth = r.dayOfMonth();
			repeatPatternEntity.month = r.month();
			return Result.ok(repeatPatternEntity);
		} else if (repeat instanceof EventRepeatRelativeMonthlyDTO r) {
			var repeatPatternEntity = fillDefaults(new EventRepeatRelativeMonthlyEntity(), startDate, repeat);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek();
			return Result.ok(repeatPatternEntity);
		} else if (repeat instanceof EventRepeatRelativeYearlyDTO r) {
			var repeatPatternEntity = fillDefaults(new EventRepeatRelativeYearlyEntity(), startDate, repeat);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek();
			repeatPatternEntity.month = r.month();
			return Result.ok(repeatPatternEntity);
		}

		throw new IllegalStateException(String.format("Unknown repeat type %s", repeat.getClass()));
	}
}
