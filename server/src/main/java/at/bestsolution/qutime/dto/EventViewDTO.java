package at.bestsolution.qutime.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import at.bestsolution.qutime.model.EventEntity;
import at.bestsolution.qutime.model.modification.EventModificationMovedEntity;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

@JsonbTypeInfo({
		@JsonbSubtype(alias = "single", type = EventViewDTO.SingleEventViewDTO.class),
		@JsonbSubtype(alias = "series-moved", type = EventViewDTO.SeriesMovedEventViewDTO.class),
		@JsonbSubtype(alias = "series", type = EventViewDTO.SeriesEventViewDTO.class)
})
public abstract class EventViewDTO implements Comparable<EventViewDTO> {
	public String key;
	public String calendarKey;
	public String title;
	public String description;
	public ZonedDateTime start;
	public ZonedDateTime end;

	@Override
	public int compareTo(EventViewDTO o) {
		var result = start.compareTo(o.start);
		if (result == 0) {
			result = end.compareTo(o.end);
			if (result == 0) {
				result = key.compareTo(o.key);
			}
		}

		return result;
	}

	public static class SingleEventViewDTO extends EventViewDTO {
		public static SingleEventViewDTO of(EventEntity event, ZoneId resultZone) {
			var result = new SingleEventViewDTO();
			result.key = event.key.toString();
			result.calendarKey = event.calendar.key.toString();
			result.title = event.title;
			result.description = event.description;
			result.start = event.start.withZoneSameInstant(resultZone);
			result.end = event.end.withZoneSameInstant(resultZone);
			return result;
		}
	}

	public static class SeriesMovedEventViewDTO extends EventViewDTO {
		public String masterEventKey;
		public ZonedDateTime originalStart;
		public ZonedDateTime originalEnd;

		public static SeriesMovedEventViewDTO of(EventModificationMovedEntity movedEntity, ZoneId resultZone) {
			var result = new SeriesMovedEventViewDTO();
			result.key = movedEntity.event.key.toString() + "_" + movedEntity.date;
			result.calendarKey = movedEntity.event.calendar.key.toString();
			result.masterEventKey = movedEntity.event.key.toString();
			result.title = movedEntity.event.title;
			result.description = movedEntity.event.description;
			result.start = movedEntity.start.withZoneSameInstant(resultZone);
			result.end = movedEntity.end.withZoneSameInstant(resultZone);

			result.originalStart = movedEntity.event.start
					.withZoneSameInstant(movedEntity.event.repeatPattern.recurrenceTimezone)
					.withZoneSameInstant(resultZone);
			result.originalEnd = movedEntity.event.end
					.withZoneSameInstant(movedEntity.event.repeatPattern.recurrenceTimezone)
					.withZoneSameInstant(resultZone);

			return result;
		}
	}

	public static class SeriesEventViewDTO extends EventViewDTO {
		public String masterEventKey;

		public static SeriesEventViewDTO of(EventEntity event, LocalDate date, ZoneId zone) {
			var start = event.start.withZoneSameInstant(event.repeatPattern.recurrenceTimezone);
			var end = event.end.withZoneSameInstant(event.repeatPattern.recurrenceTimezone);
			var dayDiff = ChronoUnit.DAYS.between(start.toLocalDate(), date);

			var adjustedStart = start.plusDays(dayDiff);
			var adjustedEnd = end.plusDays(dayDiff);

			if (!event.modifications.isEmpty()) {
				var targetDate = adjustedStart.toLocalDate();
				var modified = event.modificationsAt(targetDate)
						.stream()
						.filter(m -> m instanceof EventModificationMovedEntity)
						.findFirst()
						.isPresent();
				if (modified) {
					return null;
				}
			}

			var result = new SeriesEventViewDTO();
			result.key = event.key.toString() + "_" + date;
			result.calendarKey = event.calendar.key.toString();
			result.masterEventKey = event.key.toString();
			result.title = event.title;
			result.description = event.description;
			result.start = adjustedStart.withZoneSameInstant(zone);
			result.end = adjustedEnd.withZoneSameInstant(zone);
			return result;
		}
	}

	public static EventViewDTO of(EventEntity event, ZoneId resultZone) {
		return SingleEventViewDTO.of(event, resultZone);
	}

	public static EventViewDTO of(EventModificationMovedEntity movedEntity, ZoneId resultZone) {
		return SeriesMovedEventViewDTO.of(movedEntity, resultZone);
	}

	public static EventViewDTO of(EventEntity event, LocalDate date, ZoneId zone) {
		return SeriesEventViewDTO.of(event, date, zone);
	}
}
