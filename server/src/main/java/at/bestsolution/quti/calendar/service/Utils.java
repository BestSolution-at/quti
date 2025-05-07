package at.bestsolution.quti.calendar.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.UUID;

public class Utils {

	public static ZonedDateTime atStartOfDay(ZonedDateTime datetime) {
		return datetime.with(ChronoField.HOUR_OF_DAY, 0);
	}

	public static ZonedDateTime atEndOfDay(ZonedDateTime datetime) {
		return datetime.toLocalDateTime().with(LocalTime.parse("23:59:59")).atZone(datetime.getZone());
	}

	public static LocalDate parseLocalDate(String date, String paramName) {
		try {
			return LocalDate.parse(date);
		} catch (Throwable t) {
			throw new InvalidArgumentException("'%s' in %s is not a valid ISO-Date".formatted(date, paramName));
		}
	}

	public static UUID parseUUID(String uuid, String paramName) {
		try {
			return UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			throw new InvalidArgumentException("'%s' in %s is not a valid UUID".formatted(uuid, paramName));
		}
	}
}
