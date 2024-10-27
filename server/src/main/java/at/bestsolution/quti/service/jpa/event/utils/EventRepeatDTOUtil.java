package at.bestsolution.quti.service.jpa.event.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.dto.EventRepeatDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatAbsoluteYearlyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatDailyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatRelativeMonthlyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatRelativeYearlyDTO;
import at.bestsolution.quti.dto.EventRepeatDTO.EventRepeatWeeklyDTO;
import at.bestsolution.quti.model.EventRepeatEntity;
import at.bestsolution.quti.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatWeeklyEntity;

public class EventRepeatDTOUtil {
public static EventRepeatDTO of(EventRepeatEntity entity) {
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
		public static EventRepeatDailyDTO of(EventRepeatDailyEntity entity) {
			var result = new EventRepeatDailyDTO();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			return result;
		}

		public static EventRepeatDailyDTO of(short interval, String timeZone, LocalDate endDate) {
			var rv = new EventRepeatDailyDTO();
			rv.interval = interval;
			rv.timeZone = ZoneId.of(timeZone);
			rv.endDate = endDate;
			return rv;
		}
	}

	public class EventRepeatWeeklyDTOUtil {
		public static EventRepeatWeeklyDTO of(EventRepeatWeeklyEntity entity) {
			var result = new EventRepeatWeeklyDTO();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.daysOfWeek = entity.daysOfWeek;
			return result;
		}

		public static EventRepeatWeeklyDTO of(short interval, String timeZone, LocalDate endDate, List<DayOfWeek> daysOfWeek) {
			var result = new EventRepeatWeeklyDTO();
			result.interval = interval;
			result.endDate = endDate;
			result.timeZone = ZoneId.of(timeZone);
			result.daysOfWeek = daysOfWeek;
			return result;
		}
	}

	public class EventRepeatAbsoluteMonthlyDTOUtil {
		public static EventRepeatAbsoluteMonthlyDTO of(EventRepeatAbsoluteMonthlyEntity entity) {
			var result = new EventRepeatAbsoluteMonthlyDTO();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.dayOfMonth = entity.dayOfMonth;
			return result;
		}
	}

	public class EventRepeatAbsoluteYearlyDTOUtil {
		public static EventRepeatAbsoluteYearlyDTO of(EventRepeatAbsoluteYearlyEntity entity) {
			var result = new EventRepeatAbsoluteYearlyDTO();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.dayOfMonth = entity.dayOfMonth;
			result.month = entity.month;
			return result;
		}
	}

	public class EventRepeatRelativeMonthlyDTOUtil {
		public static EventRepeatRelativeMonthlyDTO of(EventRepeatRelativeMonthlyEntity entity) {
			var result = new EventRepeatRelativeMonthlyDTO();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.daysOfWeek = entity.daysOfWeek;
			return result;
		}
	}

	public class EventRepeatRelativeYearlyDTOUtil {
		public static EventRepeatRelativeYearlyDTO of(EventRepeatRelativeYearlyEntity entity) {
			var result = new EventRepeatRelativeYearlyDTO();
			result.interval = entity.interval;
			result.endDate = entity.endDate != null ? entity.endDate.withZoneSameInstant(entity.recurrenceTimezone).toLocalDate() : null;
			result.timeZone = entity.recurrenceTimezone;
			result.daysOfWeek = entity.daysOfWeek;
			result.month = entity.month;
			return result;
		}
	}
}
