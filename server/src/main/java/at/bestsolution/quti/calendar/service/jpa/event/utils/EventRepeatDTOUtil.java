package at.bestsolution.quti.calendar.service.jpa.event.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.calendar.service.jpa.model.EventRepeatEntity;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatWeeklyEntity;
import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.InvalidContentException;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.model.EventRepeat;
import at.bestsolution.quti.calendar.service.model.EventRepeatAbsoluteMonthly;
import at.bestsolution.quti.calendar.service.model.EventRepeatAbsoluteYearly;
import at.bestsolution.quti.calendar.service.model.EventRepeatDaily;
import at.bestsolution.quti.calendar.service.model.EventRepeatRelativeMonthly;
import at.bestsolution.quti.calendar.service.model.EventRepeatRelativeYearly;
import at.bestsolution.quti.calendar.service.model.EventRepeatWeekly;
import at.bestsolution.quti.calendar.service.model.mixins.EventRepeatDataMixin;

public class EventRepeatDTOUtil {
	public static EventRepeat.Data of(BuilderFactory factory, EventRepeatEntity entity) {
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
		public static EventRepeatDaily.Data of(BuilderFactory factory, EventRepeatDailyEntity entity) {
			var b = factory.builder(EventRepeatDaily.DataBuilder.class);
			return b
					.interval(entity.interval)
					.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate()
							: null)
					.timeZone(entity.recurrenceTimezone)
					.build();
		}

		public static EventRepeatDaily.Data of(BuilderFactory factory, short interval, String timeZone,
				LocalDate endDate) {
			var b = factory.builder(EventRepeatDaily.DataBuilder.class);
			return b
					.interval(interval)
					.timeZone(ZoneId.of(timeZone))
					.endDate(endDate)
					.build();
		}
	}

	public class EventRepeatWeeklyDTOUtil {
		public static EventRepeatWeekly.Data of(BuilderFactory factory, EventRepeatWeeklyEntity entity) {
			var b = factory.builder(EventRepeatWeekly.DataBuilder.class);
			return b
					.interval(entity.interval)
					.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate()
							: null)
					.timeZone(entity.recurrenceTimezone)
					.daysOfWeek(entity.daysOfWeek)
					.build();
		}

		public static EventRepeatWeekly.Data of(BuilderFactory factory, short interval, String timeZone,
				LocalDate endDate,
				List<DayOfWeek> daysOfWeek) {
			var b = factory.builder(EventRepeatWeekly.DataBuilder.class);
			return b
					.interval(interval)
					.endDate(endDate)
					.timeZone(ZoneId.of(timeZone))
					.daysOfWeek(daysOfWeek)
					.build();
		}
	}

	public class EventRepeatAbsoluteMonthlyDTOUtil {
		public static EventRepeatAbsoluteMonthly.Data of(BuilderFactory factory,
				EventRepeatAbsoluteMonthlyEntity entity) {
			var b = factory.builder(EventRepeatAbsoluteMonthly.DataBuilder.class);
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
		public static EventRepeatAbsoluteYearly.Data of(BuilderFactory factory,
				EventRepeatAbsoluteYearlyEntity entity) {
			var b = factory.builder(EventRepeatAbsoluteYearly.DataBuilder.class);
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
		public static EventRepeatRelativeMonthly.Data of(BuilderFactory factory,
				EventRepeatRelativeMonthlyEntity entity) {
			var b = factory.builder(EventRepeatRelativeMonthly.DataBuilder.class);
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
		public static EventRepeatRelativeYearly.Data of(BuilderFactory factory,
				EventRepeatRelativeYearlyEntity entity) {
			var b = factory.builder(EventRepeatRelativeYearly.DataBuilder.class);
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

	private static <T extends EventRepeatEntity> T fillDefaults(T repeatEntity, LocalDate startDate, EventRepeat.Data r) {
		var recurrenceTimezone = r.timeZone();
		repeatEntity.interval = r.interval();
		repeatEntity.startDate = ZonedDateTime.of(startDate, LocalTime.MIN, recurrenceTimezone);
		repeatEntity.endDate = r.endDate() == null ? null
				: Utils.atEndOfDay(ZonedDateTime.of(r.endDate(), LocalTime.NOON, recurrenceTimezone));
		repeatEntity.recurrenceTimezone = recurrenceTimezone;
		return repeatEntity;
	}

	public static EventRepeatEntity createRepeatPattern(LocalDate startDate, EventRepeat.Data repeat) {
		if (repeat instanceof EventRepeatDataMixin r) {
			if (r.interval() <= 0) {
				throw new InvalidContentException("Interval must be greater than 0");
			}
		}

		if (repeat instanceof EventRepeatDaily.Data) {
			return fillDefaults(new EventRepeatDailyEntity(), startDate, repeat);
		} else if (repeat instanceof EventRepeatWeekly.Data r) {
			if (r.daysOfWeek().isEmpty()) {
				throw new InvalidContentException("Weekdays must not be empty");
			}
			var repeatPatternEntity = fillDefaults(new EventRepeatWeeklyEntity(), startDate, repeat);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek();
			return repeatPatternEntity;
		} else if (repeat instanceof EventRepeatAbsoluteMonthly.Data r) {
			var repeatPatternEntity = fillDefaults(new EventRepeatAbsoluteMonthlyEntity(), startDate, repeat);
			repeatPatternEntity.dayOfMonth = r.dayOfMonth();
			return repeatPatternEntity;
		} else if (repeat instanceof EventRepeatAbsoluteYearly.Data r) {
			var repeatPatternEntity = fillDefaults(new EventRepeatAbsoluteYearlyEntity(), startDate, repeat);
			repeatPatternEntity.dayOfMonth = r.dayOfMonth();
			repeatPatternEntity.month = r.month();
			return repeatPatternEntity;
		} else if (repeat instanceof EventRepeatRelativeMonthly.Data r) {
			var repeatPatternEntity = fillDefaults(new EventRepeatRelativeMonthlyEntity(), startDate, repeat);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek();
			return repeatPatternEntity;
		} else if (repeat instanceof EventRepeatRelativeYearly.Data r) {
			var repeatPatternEntity = fillDefaults(new EventRepeatRelativeYearlyEntity(), startDate, repeat);
			repeatPatternEntity.daysOfWeek = r.daysOfWeek();
			repeatPatternEntity.month = r.month();
			return repeatPatternEntity;
		}

		throw new IllegalStateException(String.format("Unknown repeat type %s", repeat.getClass()));
	}
}
