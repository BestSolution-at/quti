package at.bestsolution.quti.service.jpa.calendar.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.modification.EventModificationCanceledEntity;
import at.bestsolution.quti.service.jpa.model.modification.EventModificationGenericEntity;
import at.bestsolution.quti.service.jpa.model.modification.EventModificationMovedEntity;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.model.EventView;
import at.bestsolution.quti.service.model.SeriesEventView;
import at.bestsolution.quti.service.model.SeriesMovedEventView;
import at.bestsolution.quti.service.model.SingleEventView;
import at.bestsolution.quti.service.model.mixins.EventViewDataMixin.Status;

public class EventViewDTOUtil {
	public static EventView.Data of(BuilderFactory factory, EventEntity event, ZoneId resultZone) {
		return SingleEventViewDTOUtil.of(factory, event, resultZone);
	}

	public static EventView.Data of(BuilderFactory factory, EventModificationMovedEntity movedEntity,
			ZoneId resultZone) {
		return SeriesMovedEventViewDTOUtil.of(factory, movedEntity, resultZone);
	}

	public static EventView.Data of(BuilderFactory factory, EventEntity event, LocalDate date, ZoneId zone) {
		return SeriesEventViewDTOUtil.of(factory, event, date, zone);
	}

	public static int compare(EventView.Data a, EventView.Data b) {
		var result = a.start().compareTo(b.start());
		if (result == 0) {
			result = a.end().compareTo(b.end());
			if (result == 0) {
				result = a.key().compareTo(b.key());
			}
		}

		return result;
	}

	public class SingleEventViewDTOUtil {
		public static SingleEventView.Data of(BuilderFactory factory, EventEntity event, ZoneId resultZone) {
			var b = factory.builder(SingleEventView.DataBuilder.class);
			return b
					.key(event.key.toString())
					.calendarKey(event.calendar.key.toString())
					.owner(event.calendar.owner)
					.title(event.title)
					.description(event.description)
					.start(event.start.withZoneSameInstant(resultZone))
					.end(event.end.withZoneSameInstant(resultZone))
					.tags(Objects.requireNonNullElse(event.tags, List.of()))
					.referencedCalendars(event.references.stream().map(er -> er.calendar.key.toString()).toList())
					.status(isCanceled(event) ? Status.CANCELED : Status.ACCEPTED)
					.build();
		}

		private static boolean isCanceled(EventEntity event) {
			return (!event.modifications.isEmpty())
					&& event.modifications.stream().anyMatch(m -> m instanceof EventModificationCanceledEntity);
		}
	}

	public class SeriesMovedEventViewDTOUtil {
		public static SeriesMovedEventView.Data of(BuilderFactory factory, EventModificationMovedEntity movedEntity,
				ZoneId resultZone) {
			var status = Status.ACCEPTED;

			var canceled = movedEntity.event.modificationsAt(movedEntity.date)
					.stream()
					.anyMatch(m -> m instanceof EventModificationCanceledEntity);
			if (canceled) {
				status = Status.CANCELED;
			}

			var description = movedEntity.event.modificationsAt(movedEntity.date)
					.stream()
					.filter(m -> m instanceof EventModificationGenericEntity)
					.findFirst()
					.map(m -> (EventModificationGenericEntity) m)
					.map(m -> m.description)
					.filter(Predicate.not(String::isBlank))
					.orElse(movedEntity.event.description);

			var b = factory.builder(SeriesMovedEventView.DataBuilder.class);

			return b
					.key(movedEntity.event.key.toString() + "_" + movedEntity.date)
					.calendarKey(movedEntity.event.calendar.key.toString())
					.owner(movedEntity.event.calendar.owner)
					.masterEventKey(movedEntity.event.key.toString())
					.title(movedEntity.event.title)
					.description(description)
					.start(movedEntity.start.withZoneSameInstant(resultZone))
					.end(movedEntity.end.withZoneSameInstant(resultZone))
					.tags(Objects.requireNonNullElse(movedEntity.event.tags, List.of()))
					.referencedCalendars(movedEntity.event.references.stream().map(er -> er.calendar.key.toString()).toList())
					.originalStart(movedEntity.event.start
							.withZoneSameInstant(movedEntity.event.repeatPattern.recurrenceTimezone)
							.withZoneSameInstant(resultZone))
					.originalEnd(movedEntity.event.end
							.withZoneSameInstant(movedEntity.event.repeatPattern.recurrenceTimezone)
							.withZoneSameInstant(resultZone))
					.status(status)
					.build();
		}
	}

	public class SeriesEventViewDTOUtil {
		public static SeriesEventView.Data of(BuilderFactory factory, EventEntity event, LocalDate date, ZoneId zone) {
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
				if (canceled) {
					status = Status.CANCELED;
				}

				description = event.modificationsAt(targetDate)
						.stream()
						.filter(m -> m instanceof EventModificationGenericEntity)
						.findFirst()
						.map(m -> (EventModificationGenericEntity) m)
						.map(m -> m.description)
						.filter(Predicate.not(String::isBlank))
						.orElse(description);
			}

			var b = factory.builder(SeriesEventView.DataBuilder.class);
			return b
					.key(event.key.toString() + "_" + date)
					.calendarKey(event.calendar.key.toString())
					.owner(event.calendar.owner)
					.masterEventKey(event.key.toString())
					.title(event.title)
					.description(description)
					.start(adjustedStart.withZoneSameInstant(zone))
					.end(adjustedEnd.withZoneSameInstant(zone))
					.tags(Objects.requireNonNullElse(event.tags, List.of()))
					.referencedCalendars(event.references.stream().map(er -> er.calendar.key.toString()).toList())
					.status(status)
					.build();
		}
	}
}
