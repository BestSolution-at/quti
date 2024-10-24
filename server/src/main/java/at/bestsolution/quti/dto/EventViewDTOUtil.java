package at.bestsolution.quti.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import at.bestsolution.quti.dto.EventViewDTO.SeriesEventViewDTO;
import at.bestsolution.quti.dto.EventViewDTO.SeriesMovedEventViewDTO;
import at.bestsolution.quti.dto.EventViewDTO.SingleEventViewDTO;
import at.bestsolution.quti.dto.EventViewDTO.Status;
import at.bestsolution.quti.model.EventEntity;
import at.bestsolution.quti.model.modification.EventModificationCanceledEntity;
import at.bestsolution.quti.model.modification.EventModificationGenericEntity;
import at.bestsolution.quti.model.modification.EventModificationMovedEntity;

public class EventViewDTOUtil {
	public static EventViewDTO of(EventEntity event, ZoneId resultZone) {
		return SingleEventViewDTOUtil.of(event, resultZone);
	}

	public static EventViewDTO of(EventModificationMovedEntity movedEntity, ZoneId resultZone) {
		return SeriesMovedEventViewDTOUtil.of(movedEntity, resultZone);
	}

	public static EventViewDTO of(EventEntity event, LocalDate date, ZoneId zone) {
		return SeriesEventViewDTOUtil.of(event, date, zone);
	}

	public static int compare(EventViewDTO a, EventViewDTO b) {
		var result = a.start.compareTo(b.start);
		if (result == 0) {
			result = a.end.compareTo(b.end);
			if (result == 0) {
				result = a.key.compareTo(b.key);
			}
		}

		return result;
	}

	public class SingleEventViewDTOUtil {
		public static SingleEventViewDTO of(EventEntity event, ZoneId resultZone) {
			var result = new SingleEventViewDTO();
			result.key = event.key.toString();
			result.calendarKey = event.calendar.key.toString();
			result.owner = event.calendar.owner;
			result.title = event.title;
			result.description = event.description;
			result.start = event.start.withZoneSameInstant(resultZone);
			result.end = event.end.withZoneSameInstant(resultZone);
			result.tags = Objects.requireNonNullElse(event.tags, List.of());
			result.referencedCalendars = event.references.stream().map( er -> er.calendar.key.toString()).toList();
			result.status = isCanceled(event) ? Status.CANCELED : Status.ACCEPTED;
			return result;
		}

		private static boolean isCanceled(EventEntity event) {
			return (!event.modifications.isEmpty()) && event.modifications.stream().anyMatch( m -> m instanceof EventModificationCanceledEntity);
		}
	}

	public class SeriesMovedEventViewDTOUtil {
		public static SeriesMovedEventViewDTO of(EventModificationMovedEntity movedEntity, ZoneId resultZone) {
			var status = Status.ACCEPTED;

			var canceled = movedEntity.event.modificationsAt(movedEntity.date)
				.stream()
				.anyMatch(m -> m instanceof EventModificationCanceledEntity);
			if( canceled ) {
				status = Status.CANCELED;
			}

			var description = movedEntity.event.modificationsAt(movedEntity.date)
					.stream()
					.filter(m -> m instanceof EventModificationGenericEntity)
					.findFirst()
					.map( m -> (EventModificationGenericEntity)m)
					.map( m -> m.description)
					.filter(Predicate.not(String::isBlank))
					.orElse(movedEntity.event.description);

			var result = new SeriesMovedEventViewDTO();
			result.key = movedEntity.event.key.toString() + "_" + movedEntity.date;
			result.calendarKey = movedEntity.event.calendar.key.toString();
			result.owner = movedEntity.event.calendar.owner;
			result.masterEventKey = movedEntity.event.key.toString();
			result.title = movedEntity.event.title;
			result.description = description;
			result.start = movedEntity.start.withZoneSameInstant(resultZone);
			result.end = movedEntity.end.withZoneSameInstant(resultZone);
			result.tags = Objects.requireNonNullElse(movedEntity.event.tags, List.of());
			result.referencedCalendars = movedEntity.event.references.stream().map( er -> er.calendar.key.toString()).toList();

			result.originalStart = movedEntity.event.start
					.withZoneSameInstant(movedEntity.event.repeatPattern.recurrenceTimezone)
					.withZoneSameInstant(resultZone);
			result.originalEnd = movedEntity.event.end
					.withZoneSameInstant(movedEntity.event.repeatPattern.recurrenceTimezone)
					.withZoneSameInstant(resultZone);
			result.status = status;

			return result;
		}
	}

	public class SeriesEventViewDTOUtil {
		public static SeriesEventViewDTO of(EventEntity event, LocalDate date, ZoneId zone) {
			var start = event.start.withZoneSameInstant(event.repeatPattern.recurrenceTimezone);
			var end = event.end.withZoneSameInstant(event.repeatPattern.recurrenceTimezone);
			var dayDiff = ChronoUnit.DAYS.between(start.toLocalDate(), date);

			var adjustedStart = start.plusDays(dayDiff);
			var adjustedEnd = end.plusDays(dayDiff);

			var status = Status.ACCEPTED;
			var description = event.description;
			if (!event.modifications.isEmpty()) {
				var targetDate = adjustedStart.toLocalDate();
				var modified = event.modificationsAt(targetDate)
						.stream()
						.anyMatch(m -> m instanceof EventModificationMovedEntity);
				if (modified) {
					return null;
				}

				var canceled = event.modificationsAt(targetDate)
					.stream()
					.anyMatch(m -> m instanceof EventModificationCanceledEntity);
				if( canceled ) {
					status = Status.CANCELED;
				}

				description = event.modificationsAt(targetDate)
					.stream()
					.filter(m -> m instanceof EventModificationGenericEntity)
					.findFirst()
					.map( m -> (EventModificationGenericEntity)m)
					.map( m -> m.description)
					.filter(Predicate.not(String::isBlank))
					.orElse(description);
			}

			var result = new SeriesEventViewDTO();
			result.key = event.key.toString() + "_" + date;
			result.calendarKey = event.calendar.key.toString();
			result.owner = event.calendar.owner;
			result.masterEventKey = event.key.toString();
			result.title = event.title;
			result.description = description;
			result.start = adjustedStart.withZoneSameInstant(zone);
			result.end = adjustedEnd.withZoneSameInstant(zone);
			result.tags = Objects.requireNonNullElse(event.tags, List.of());
			result.referencedCalendars = event.references.stream().map( er -> er.calendar.key.toString()).toList();
			result.status = status;
			return result;
		}
	}
}
