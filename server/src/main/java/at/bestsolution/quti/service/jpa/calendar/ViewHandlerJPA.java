package at.bestsolution.quti.service.jpa.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.impl.CalendarServiceImpl;
import at.bestsolution.quti.service.jpa.BaseReadonlyHandler;
import at.bestsolution.quti.service.jpa.RepeatUtils;
import at.bestsolution.quti.service.jpa.calendar.utils.EventViewDTOUtil;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventReferenceEntity;
import at.bestsolution.quti.service.jpa.model.modification.EventModificationMovedEntity;
import at.bestsolution.quti.service.model.EventView;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class ViewHandlerJPA extends BaseReadonlyHandler implements CalendarServiceImpl.EventViewHandler {

	@Inject
	public ViewHandlerJPA(EntityManager em) {
		super(em);
	}

	public List<EventView.Data> eventView(BuilderFactory factory, String calendarKey, LocalDate start,
			LocalDate end,
			ZoneId timezone, ZoneId resultZone) {
		Objects.requireNonNull(calendarKey, "calendarKey must not be null");
		Objects.requireNonNull(start, "start must not be null");
		Objects.requireNonNull(end, "end must not be null");
		Objects.requireNonNull(timezone, "timezone must not be null");

		var parsedCalendarKey = Utils.parseUUID(calendarKey, "request path");

		if (start.isAfter(end)) {
			throw new IllegalArgumentException(String.format("start-date '%s' must not be past end-date '%s'", start, end));
		}

		if (resultZone == null) {
			resultZone = timezone;
		}

		var startDatetime = ZonedDateTime.of(start, LocalTime.MIN, timezone);
		var endDatetime = ZonedDateTime.of(end, LocalTime.MAX, timezone);

		var result = new ArrayList<EventView.Data>();
		result.addAll(findOneTimeEvents(factory, parsedCalendarKey, startDatetime, endDatetime, resultZone));
		result.addAll(
				findOneTimeReferencedEvents(factory, parsedCalendarKey, startDatetime, endDatetime, resultZone));

		result.addAll(findMovedSeriesEvents(factory, parsedCalendarKey, startDatetime, endDatetime, resultZone));
		result.addAll(
				findMovedSeriesReferencedEvents(factory, parsedCalendarKey, startDatetime, endDatetime, resultZone));

		result.addAll(findSeriesEvents(factory, parsedCalendarKey, startDatetime, endDatetime, resultZone));
		result
				.addAll(findSeriesReferencedEvents(factory, parsedCalendarKey, startDatetime, endDatetime, resultZone));

		Collections.sort(result, EventViewDTOUtil::compare);

		return result;
	}

	private List<EventView.Data> findOneTimeEvents(BuilderFactory factory, UUID calendarKey,
			ZonedDateTime startDatetime,
			ZonedDateTime endDatetime,
			ZoneId resultZone) {
		var query = em().createQuery("""
					FROM
						Event e
					WHERE
						e.calendar.key = :calendarKey
					AND e.repeatPattern IS NULL
					AND (
						e.start <= :endDatetime
						AND
						e.end >= :startDatetime
					)
				""", EventEntity.class);
		query.setParameter("calendarKey", calendarKey);
		query.setParameter("startDatetime", startDatetime);
		query.setParameter("endDatetime", endDatetime);

		return query.getResultList()
				.stream()
				.map(event -> EventViewDTOUtil.of(factory, event, resultZone))
				.toList();
	}

	private List<EventView.Data> findOneTimeReferencedEvents(BuilderFactory factory, UUID calendarKey,
			ZonedDateTime startDatetime,
			ZonedDateTime endDatetime, ZoneId resultZone) {
		var query = em().createQuery("""
					FROM
						EventReference er
					JOIN FETCH er.event e
					WHERE
						er.calendar.key = :calendarKey
					AND e.repeatPattern IS NULL
					AND (
						e.start <= :endDatetime
						AND
						e.end >= :startDatetime
					)
				""", EventReferenceEntity.class);

		query.setParameter("calendarKey", calendarKey);
		query.setParameter("startDatetime", startDatetime);
		query.setParameter("endDatetime", endDatetime);

		return query.getResultList()
				.stream()
				.map(eventReference -> EventViewDTOUtil.of(factory, eventReference.event, resultZone))
				.toList();
	}

	private List<EventView.Data> findMovedSeriesEvents(BuilderFactory factory, UUID calendarKey,
			ZonedDateTime startDatetime,
			ZonedDateTime endDatetime, ZoneId resultZone) {
		var query = em().createQuery("""
					FROM
						EventModificationMoved em
					JOIN FETCH em.event
					LEFT JOIN FETCH em.event.modifications
					WHERE
						em.event.calendar.key = :calendarKey
					AND (
						em.start <= :endDatetime
						AND
						em.end >= :startDatetime
					)
				""", EventModificationMovedEntity.class);
		query.setParameter("calendarKey", calendarKey);
		query.setParameter("startDatetime", startDatetime);
		query.setParameter("endDatetime", endDatetime);

		return query.getResultList()
				.stream()
				.map(eventModification -> EventViewDTOUtil.of(factory, eventModification, resultZone))
				.toList();
	}

	private List<EventView.Data> findMovedSeriesReferencedEvents(BuilderFactory factory, UUID calendarKey,
			ZonedDateTime startDatetime,
			ZonedDateTime endDatetime, ZoneId resultZone) {
		var query = em().createQuery("""
				FROM
					EventModificationMoved em
				JOIN FETCH em.event e
				JOIN FETCH em.event.references r
				WHERE
					r.calendar.key = :calendarKey
				AND (
					em.start <= :endDatetime
					AND
					em.end >= :startDatetime
				)
				""", EventModificationMovedEntity.class);
		query.setParameter("calendarKey", calendarKey);
		query.setParameter("startDatetime", startDatetime);
		query.setParameter("endDatetime", endDatetime);

		var result = query.getResultList()
				.stream()
				.map(eventModification -> EventViewDTOUtil.of(factory, eventModification, resultZone))
				.toList();

		// now fill the modifications
		query = em().createQuery("""
				FROM
					EventModificationMoved em
				JOIN FETCH em.event e
				LEFT JOIN FETCH em.event.modifications
				JOIN em.event.references r
				WHERE
					r.calendar.key = :calendarKey
				AND (
					em.start <= :endDatetime
					AND
					em.end >= :startDatetime
				)
				""", EventModificationMovedEntity.class);
		query.setParameter("calendarKey", calendarKey);
		query.setParameter("startDatetime", startDatetime);
		query.setParameter("endDatetime", endDatetime);
		query.getResultList();

		return result;
	}

	private List<EventView.Data> findSeriesEvents(BuilderFactory factory, UUID calendarKey,
			ZonedDateTime startDatetime,
			ZonedDateTime endDatetime,
			ZoneId resultZone) {
		var query = em().createQuery("""
					FROM
							Event e
					LEFT JOIN FETCH e.modifications
					JOIN FETCH e.repeatPattern
					WHERE
							e.calendar.key = :calendarKey
					AND e.repeatPattern.startDate <= :endDate
					AND (
							e.repeatPattern.endDate IS NULL
							OR
							e.repeatPattern.endDate >= :startDate
					)
				""", EventEntity.class);
		query.setParameter("calendarKey", calendarKey);
		query.setParameter("startDate", startDatetime);
		query.setParameter("endDate", endDatetime);

		return query.getResultList().stream()
				.flatMap(event -> fromRepeat(factory, event, startDatetime, endDatetime, resultZone))
				.toList();
	}

	private List<EventView.Data> findSeriesReferencedEvents(BuilderFactory factory, UUID calendarKey,
			ZonedDateTime startDatetime,
			ZonedDateTime endDatetime, ZoneId resultZone) {
		var query = em().createQuery("""
					FROM
						EventReference er
					JOIN FETCH er.event e
					LEFT JOIN FETCH er.event.modifications
					JOIN FETCH e.repeatPattern
					WHERE
						er.calendar.key = :calendarKey
					AND e.repeatPattern.startDate <= :endDate
					AND (
							e.repeatPattern.endDate IS NULL
							OR
							e.repeatPattern.endDate >= :startDate
					)
				""", EventReferenceEntity.class);

		query.setParameter("calendarKey", calendarKey);
		query.setParameter("startDate", startDatetime);
		query.setParameter("endDate", endDatetime);

		return query.getResultList().stream()
				.flatMap(eventReference -> fromRepeat(factory, eventReference.event, startDatetime, endDatetime, resultZone))
				.toList();
	}

	private static Stream<EventView.Data> fromRepeat(BuilderFactory factory, EventEntity entity,
			ZonedDateTime startDatetime,
			ZonedDateTime endDatetime, ZoneId resultZone) {
		return RepeatUtils.fromRepeat(entity, startDatetime, endDatetime)
				.map(date -> EventViewDTOUtil.of(factory, entity, date, resultZone))
				.filter(Objects::nonNull);
	}
}
