package at.bestsolution.quti.service.jpa.event.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatWeeklyEntity;
import at.bestsolution.quti.service.DTOBuilderFactory;
import at.bestsolution.quti.service.dto.EventRepeatDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatDailyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatRelativeYearlyDTO;
import at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatWeeklyDTO;

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
				.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null)
				.timeZone(entity.recurrenceTimezone)
				.build();
		}

		public static EventRepeatDailyDTO of(DTOBuilderFactory factory, short interval, String timeZone, LocalDate endDate) {
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
				.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null)
				.timeZone(entity.recurrenceTimezone)
				.daysOfWeek(entity.daysOfWeek)
				.build();
		}

		public static EventRepeatWeeklyDTO of(DTOBuilderFactory factory, short interval, String timeZone, LocalDate endDate, List<DayOfWeek> daysOfWeek) {
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
				.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null)
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
				.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null)
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
				.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null)
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
				.endDate(entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null)
				.timeZone(entity.recurrenceTimezone)
				.daysOfWeek(entity.daysOfWeek)
				.month(entity.month)
				.build();
		}
	}
}
