package at.bestsolution.quti.service.jpa.event.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.model.EventRepeatEntity;
import at.bestsolution.quti.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatWeeklyEntity;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatAbsoluteMonthlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatAbsoluteYearlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatDailyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatRelativeMonthlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatRelativeYearlyDTOImpl;
import at.bestsolution.quti.rest.dto.EventRepeatDTOImpl.EventRepeatWeeklyDTOImpl;

public class EventRepeatDTOUtil {
public static EventRepeatDTOImpl of(EventRepeatEntity entity) {
		if (entity == null) {
			return null;
		}

		if (entity instanceof EventRepeatAbsoluteMonthlyEntity e) {
			return EventRepeatAbsoluteMonthlyDTOUtil.of(e);
		} else if (entity instanceof EventRepeatAbsoluteYearlyEntity e) {
			return EventRepeatAbsoluteYearlyDTOUtil.of(e);
		} else if (entity instanceof EventRepeatDailyEntity e) {
			return EventRepeatDailyDTOUtil.of(e);
		} else if (entity instanceof EventRepeatRelativeMonthlyEntity e) {
			return EventRepeatRelativeMonthlyDTOUtil.of(e);
		} else if (entity instanceof EventRepeatRelativeYearlyEntity e) {
			return EventRepeatRelativeYearlyDTOUtil.of(e);
		} else if (entity instanceof EventRepeatWeeklyEntity e) {
			return EventRepeatWeeklyDTOUtil.of(e);
		}
		throw new IllegalArgumentException("Unknown entity '" + entity + "'");
	}

	public class EventRepeatDailyDTOUtil {
		public static EventRepeatDailyDTOImpl of(EventRepeatDailyEntity entity) {
			var result = new EventRepeatDailyDTOImpl();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			return result;
		}

		public static EventRepeatDailyDTOImpl of(short interval, String timeZone, LocalDate endDate) {
			var rv = new EventRepeatDailyDTOImpl();
			rv.interval = interval;
			rv.timeZone = ZoneId.of(timeZone);
			rv.endDate = endDate;
			return rv;
		}
	}

	public class EventRepeatWeeklyDTOUtil {
		public static EventRepeatWeeklyDTOImpl of(EventRepeatWeeklyEntity entity) {
			var result = new EventRepeatWeeklyDTOImpl();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.daysOfWeek = entity.daysOfWeek;
			return result;
		}

		public static EventRepeatWeeklyDTOImpl of(short interval, String timeZone, LocalDate endDate, List<DayOfWeek> daysOfWeek) {
			var result = new EventRepeatWeeklyDTOImpl();
			result.interval = interval;
			result.endDate = endDate;
			result.timeZone = ZoneId.of(timeZone);
			result.daysOfWeek = daysOfWeek;
			return result;
		}
	}

	public class EventRepeatAbsoluteMonthlyDTOUtil {
		public static EventRepeatAbsoluteMonthlyDTOImpl of(EventRepeatAbsoluteMonthlyEntity entity) {
			var result = new EventRepeatAbsoluteMonthlyDTOImpl();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.dayOfMonth = entity.dayOfMonth;
			return result;
		}
	}

	public class EventRepeatAbsoluteYearlyDTOUtil {
		public static EventRepeatAbsoluteYearlyDTOImpl of(EventRepeatAbsoluteYearlyEntity entity) {
			var result = new EventRepeatAbsoluteYearlyDTOImpl();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.dayOfMonth = entity.dayOfMonth;
			result.month = entity.month;
			return result;
		}
	}

	public class EventRepeatRelativeMonthlyDTOUtil {
		public static EventRepeatRelativeMonthlyDTOImpl of(EventRepeatRelativeMonthlyEntity entity) {
			var result = new EventRepeatRelativeMonthlyDTOImpl();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.daysOfWeek = entity.daysOfWeek;
			return result;
		}
	}

	public class EventRepeatRelativeYearlyDTOUtil {
		public static EventRepeatRelativeYearlyDTOImpl of(EventRepeatRelativeYearlyEntity entity) {
			var result = new EventRepeatRelativeYearlyDTOImpl();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.daysOfWeek = entity.daysOfWeek;
			result.month = entity.month;
			return result;
		}
	}
}
