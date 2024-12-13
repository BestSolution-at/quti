package at.bestsolution.quti.service.jpa;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatWeeklyEntity;

public class RepeatUtils {
	private static ZonedDateTime boxStartDateTime(EventRepeatEntity entity, ZonedDateTime startDatetime) {
		if (startDatetime.isBefore(entity.startDate)) {
			return entity.startDate.withZoneSameInstant(entity.recurrenceTimezone);
		}
		return startDatetime.withZoneSameInstant(entity.recurrenceTimezone);
	}

	private static ZonedDateTime boxEndDateTime(EventRepeatEntity entity, ZonedDateTime endDatetime) {
		if (entity.endDate == null) {
			return endDatetime.withZoneSameInstant(entity.recurrenceTimezone);
		} else {
			if (endDatetime.isBefore(entity.endDate)) {
				return endDatetime.withZoneSameInstant(entity.recurrenceTimezone);
			}
			return entity.endDate.withZoneSameInstant(entity.recurrenceTimezone);
		}
	}

	public static Stream<LocalDate> fromRepeat(EventEntity entity, ZonedDateTime startDatetime,
			ZonedDateTime endDatetime) {
		var boxStart = boxStartDateTime(entity.repeatPattern, startDatetime);
		var boxEnd = boxEndDateTime(entity.repeatPattern, endDatetime);

		if (entity.repeatPattern instanceof EventRepeatDailyEntity r) {
			return fromRepeatDaily(r.interval, entity.start, boxStart, boxEnd);
		} else if (entity.repeatPattern instanceof EventRepeatWeeklyEntity r) {
			return fromRepeatWeekly(r.daysOfWeek, r.interval, entity.start, boxStart, boxEnd);
		} else if (entity.repeatPattern instanceof EventRepeatAbsoluteMonthlyEntity r) {
			return fromRepeatAbsoluteMonthly(r, entity, boxStart, boxEnd);
		} else if (entity.repeatPattern instanceof EventRepeatAbsoluteYearlyEntity r) {
			return fromRepeatAbsoluteYearly(r, entity, boxStart, boxEnd);
		} else if (entity.repeatPattern instanceof EventRepeatRelativeMonthlyEntity r) {
			return fromRepeatRelativeMonthly(r, entity, boxStart, boxEnd);
		} else if (entity.repeatPattern instanceof EventRepeatRelativeYearlyEntity r) {
			return fromRepeatRelativeYearly(r, entity, boxStart, boxEnd);
		}
		throw new IllegalStateException(String.format("Unknown repeatPattern '%s'", entity.repeatPattern));
	}

	public static Stream<LocalDate> fromRepeatDaily(int interval, ZonedDateTime eventStart, ZonedDateTime startDatetime,
		ZonedDateTime endDatetime) {
			List<LocalDate> dates = new ArrayList<>();

			var days = ChronoUnit.DAYS.between(
				eventStart.toLocalDate(),
				startDatetime.toLocalDate()
			) % interval;

			var currentDate = days == 0 ? startDatetime : startDatetime.plusDays(interval - days);

			while (currentDate.isBefore(endDatetime)) {
				dates.add(currentDate.toLocalDate());
				currentDate = currentDate.plusDays(interval);
			}

			return dates.stream();
		}

	public static Stream<LocalDate> fromRepeatWeekly(List<DayOfWeek> daysOfWeek, int interval,
		ZonedDateTime eventStart, ZonedDateTime startDatetime, ZonedDateTime endDatetime) {
		List<LocalDate> dates = new ArrayList<>();

		for (DayOfWeek day : daysOfWeek) {
			var currentDate = startDatetime.with(TemporalAdjusters.nextOrSame(day));
			var diff = eventStart.toLocalDate().until(currentDate.toLocalDate(), ChronoUnit.WEEKS);
			currentDate = currentDate.plusWeeks(diff % interval);

			while (currentDate.isBefore(endDatetime)) {
				dates.add(currentDate.toLocalDate());
				currentDate = currentDate.plusWeeks(interval);
			}
		}

		return dates.stream().sorted();
	}

	private static Stream<LocalDate> fromRepeatAbsoluteMonthly(EventRepeatAbsoluteMonthlyEntity repeat,
			EventEntity entity, ZonedDateTime startDatetime, ZonedDateTime endDatetime) {
		return Stream.empty();
	}

	private static Stream<LocalDate> fromRepeatAbsoluteYearly(EventRepeatAbsoluteYearlyEntity repeat, EventEntity entity,
			ZonedDateTime startDatetime, ZonedDateTime endDatetime) {
		return Stream.empty();
	}

	private static Stream<LocalDate> fromRepeatRelativeMonthly(EventRepeatRelativeMonthlyEntity repeat,
			EventEntity entity, ZonedDateTime startDatetime, ZonedDateTime endDatetime) {
		return Stream.empty();
	}

	private static Stream<LocalDate> fromRepeatRelativeYearly(EventRepeatRelativeYearlyEntity repeat, EventEntity entity,
			ZonedDateTime startDatetime, ZonedDateTime endDatetime) {
		return Stream.empty();
	}
}
