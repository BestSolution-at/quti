package at.bestsolution.quti.service.jpa.event;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.NotFoundException;
import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventRepeat;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.calendar.utils.CalendarUtils;
import at.bestsolution.quti.service.jpa.event.utils.EventRepeatDTOUtil;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventReferenceEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class UpdateHandlerJPA extends BaseHandler implements EventService.UpdateHandler {

	@Inject
	public UpdateHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void update(BuilderFactory factory, String calendarKey, String eventKey, Event.Patch patch) {
		var parsedCalendarKey = Utils._parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils._parseUUID(eventKey, "in path");

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
		patch.fullday().map(v -> v != null && v).accept(entity::fullday);
		patch.start().ifPresent(v -> handleStartChange(em, entity, v));
		patch.end().ifPresent(entity::end);

		patch.referencedCalendars().ifPresent(refs -> handleReferencedCalendardChange(em, entity, refs));
		patch.repeat().accept(r -> handleRepeatChange(em, entity, r));

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
		entity.start = v;
	}

	private static void handleReferencedCalendardChange(EntityManager em, EventEntity entity, List<String> refs) {
		var refSet = new HashSet<>(refs);
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
				.map(calendar -> {
					var ref = new EventReferenceEntity();
					ref.calendar = calendar;
					ref.event = entity;
					return ref;
				}).toList();
		;
	}

	private static void handleRepeatChange(EntityManager em, EventEntity entity, EventRepeat.Data repeat) {
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
}
