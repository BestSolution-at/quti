package at.bestsolution.quti.service.jpa.event.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Predicate;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
import jakarta.persistence.EntityManager;

public class RepeatBaseUpdater {
	public static Result<Void> handleTimezone(EventRepeatEntity r, ZoneId v) {
		if (v == null) {
			return Result.invalidContent("Setting timeZone to NULL is not allowed");
		}
		r.recurrenceTimezone = v;
		r.startDate = r.startDate.withZoneSameInstant(v);
		return Result.OK;
	}

	public static Result<Void> handleEndDate(EventRepeatEntity r, LocalDate v) {
		if (v == null) {
			r.endDate = null;
			return Result.OK;
		} else {
			var endDatetime = Utils.atEndOfDay(ZonedDateTime.of(v, LocalTime.NOON,
					r.recurrenceTimezone));
			if (endDatetime.isBefore(r.startDate)) {
				return Result.invalidContent(
						"Repeating end '%s' is before the repeat start '%s'",
						endDatetime.toLocalDate(),
						r.startDate.toLocalDate());
			}
			r.endDate = endDatetime;
		}
		return Result.OK;
	}

	public static Result<Void> handleInterval(EventRepeatEntity r, short v) {
		if (v <= 0) {
			return Result.invalidContent("Interval must be greater than 0");
		}
		r.interval = v;
		return Result.OK;
	}

	public static <T extends Enum<?>> void clearModificationProps(EntityManager em, EventEntity event, List<T> props,
			Predicate<T> test) {
		if (props.stream().anyMatch(test)) {
			event.modifications.forEach(em::remove);
		}
	}
}
