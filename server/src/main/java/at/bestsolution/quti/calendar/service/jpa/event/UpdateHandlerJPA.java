package at.bestsolution.quti.calendar.service.jpa.event;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.NotFoundException;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.impl.EventServiceImpl;
import at.bestsolution.quti.calendar.service.jpa.BaseHandler;
import at.bestsolution.quti.calendar.service.jpa.calendar.utils.CalendarUtils;
import at.bestsolution.quti.calendar.service.jpa.event.utils.EventRepeatDTOUtil;
import at.bestsolution.quti.calendar.service.jpa.event.utils.EventUtils;
import at.bestsolution.quti.calendar.service.jpa.model.EventEntity;
import at.bestsolution.quti.calendar.service.jpa.model.EventReferenceEntity;
import at.bestsolution.quti.calendar.service.model.Event;
import at.bestsolution.quti.calendar.service.model.EventRepeat;
import at.bestsolution.quti.calendar.service.model.Event.Patch.ReferencedCalendarsMergeChange;
import at.bestsolution.quti.calendar.service.model.Event.Patch.ReferencedCalendarsSetChange;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class UpdateHandlerJPA extends BaseHandler implements EventServiceImpl.UpdateHandler {

	@Inject
	public UpdateHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void update(BuilderFactory factory, String calendarKey, String eventKey, Event.Patch patch) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

		Objects.requireNonNull(patch);

		var em = em();
		var query = em.createQuery("""
				FROM
					Event e
				WHERE
					e.key = :eventKey
				AND e.calendar.key = :calendarKey
				""",
				EventEntity.class);
		query.setParameter("eventKey", parsedEventKey);
		query.setParameter("calendarKey", parsedCalendarKey);

		var result = query.getResultList();
		if (result.size() == 0) {
			throw new NotFoundException("Could not find event '%s' in calendar '%s'".formatted(parsedEventKey,
					parsedCalendarKey));
		} else if (result.size() > 1) {
			throw new IllegalStateException("Multiple matching events for '%s' are found.".formatted(parsedEventKey));
		}

		var entity = result.get(0);

		patch.title().ifPresent(entity::title);
		patch.description().accept(entity::description);
		// Update before start/end to handle fullday changes correctly
		patch.fullday().map(v -> v != null && v).accept(entity::fullday);
		patch.start().ifPresent(v -> handleStartChange(em, entity, v));
		patch.end().map(v -> entity.fullday ? Utils.atEndOfDay(v) : v).ifPresent(entity::end);

		patch.referencedCalendars()
				.ifPresent(change -> {
					if (change instanceof ReferencedCalendarsSetChange c) {
						handleReferencedCalendardChange(em, entity, c);
					} else if (change instanceof ReferencedCalendarsMergeChange c) {
						handleReferencedCalendardChange(em, entity, c);
					}
				});
		patch.repeat().accept(r -> {
			if (r == null) {
				handleRepeatSetChange(em, entity, null);
			} else {
				if (r instanceof EventRepeat.Data d) {
					handleRepeatSetChange(em, entity, d);
				} else {
					new UnsupportedOperationException("Delta change not yet implemented");
				}
			}
		});
		patch.tags().ifPresent(change -> {

		});

		EventUtils.validateEvent(entity);
	}

	private static void handleStartChange(EntityManager em, EventEntity entity, ZonedDateTime v) {
		if (entity.repeatPattern != null) {
			var newStart = ZonedDateTime.of(v.toLocalDate(), LocalTime.MIN,
					entity.repeatPattern.recurrenceTimezone);
			var current = entity.repeatPattern.startDate.withZoneSameInstant(entity.repeatPattern.recurrenceTimezone);
			// if the repeat date changes we need to clear all modifications
			if (!newStart.equals(current)) {
				entity.repeatPattern.startDate = newStart;
				entity.modifications.forEach(em::remove);
			}
		}
		entity.start = entity.fullday ? Utils.atStartOfDay(v) : v;
	}

	private static void handleReferencedCalendardChange(EntityManager em, EventEntity entity,
			ReferencedCalendarsSetChange change) {
		var refSet = new HashSet<>(change.elements());
		// Remove all references from DB who are not there anymore
		entity.references.stream()
				.filter(r -> !refSet.contains(r.calendar.key.toString()))
				.forEach(em::remove);
		// Remove existing to that only the new references are there
		refSet.removeAll(entity.references.stream().map(r -> r.calendar.key.toString()).toList());
		refSet.stream()
				.map(UUID::fromString)
				.map(key -> CalendarUtils.calendar(em, key))
				.filter(Objects::nonNull)
				.forEach(calendar -> {
					var ref = new EventReferenceEntity();
					ref.calendar = calendar;
					ref.event = entity;
					em.persist(ref);
				});
		;
	}

	private static void handleReferencedCalendardChange(EntityManager em, EventEntity entity,
			ReferencedCalendarsMergeChange change) {
		throw new UnsupportedOperationException();
	}

	private static void handleRepeatSetChange(EntityManager em, EventEntity entity, EventRepeat.Data repeat) {
		if (entity.repeatPattern != null) {
			entity.modifications.forEach(em::remove);
			em.remove(entity.repeatPattern);
		}

		if (repeat == null) {
			entity.repeatPattern = null;
		} else {
			var rv = EventRepeatDTOUtil.createRepeatPattern(entity.start.toLocalDate(), repeat);

			entity.repeatPattern = rv;
			em.persist(entity.repeatPattern);
		}
	}

	private static void handleRepeatDeltaChange(EntityManager em, EventEntity entity, EventRepeat.Patch repeat) {
		if (entity.repeatPattern == null) {
			throw new IllegalStateException("Event has no repeat pattern to update");
		}

	}
}
