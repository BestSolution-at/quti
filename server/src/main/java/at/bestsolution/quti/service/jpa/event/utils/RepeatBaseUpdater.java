package at.bestsolution.quti.service.jpa.event.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Predicate;

import at.bestsolution.quti.service.InvalidContentException;
import at.bestsolution.quti.service.Utils;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
import jakarta.persistence.EntityManager;

public class RepeatBaseUpdater {
	public static void handleTimezone(EventRepeatEntity r, ZoneId v) {
		if (v == null) {
			throw new InvalidContentException("Setting timeZone to NULL is not allowed");
		}
		r.recurrenceTimezone = v;
		r.startDate = r.startDate.withZoneSameInstant(v);
	}

	public static void handleEndDate(EventRepeatEntity r, LocalDate v) {
		if (v == null) {
			r.endDate = null;
			return;
		} else {
			var endDatetime = Utils.atEndOfDay(ZonedDateTime.of(v, LocalTime.NOON,
					r.recurrenceTimezone));
			if (endDatetime.isBefore(r.startDate)) {
				throw new InvalidContentException(
						"Repeating end '%s' is before the repeat start '%s'".formatted(endDatetime.toLocalDate(),
								r.startDate.toLocalDate()));
			}
			r.endDate = endDatetime;
		}
	}

	public static void handleInterval(EventRepeatEntity r, short v) {
		if (v <= 0) {
			throw new InvalidContentException("Interval must be greater than 0");
		}
		r.interval = v;
	}

	public static <T extends Enum<?>> void clearModificationProps(EntityManager em, EventEntity event, List<T> props,
			Predicate<T> test) {
		if (props.stream().anyMatch(test)) {
			event.modifications.forEach(em::remove);
		}
	}
}
