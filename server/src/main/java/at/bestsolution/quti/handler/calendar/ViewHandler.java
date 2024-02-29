package at.bestsolution.quti.handler.calendar;


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

import at.bestsolution.quti.dto.EventViewDTO;
import at.bestsolution.quti.handler.BaseReadonlyHandler;
import at.bestsolution.quti.handler.RepeatUtils;
import at.bestsolution.quti.model.EventEntity;
import at.bestsolution.quti.model.EventReferenceEntity;
import at.bestsolution.quti.model.modification.EventModificationMovedEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class ViewHandler extends BaseReadonlyHandler {

	@Inject
	public ViewHandler(EntityManager em) {
		super(em);
	}

	public List<EventViewDTO> view(UUID calendarKey, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultZone) {
		Objects.requireNonNull(calendarKey, "calendarKey must not be null");
		Objects.requireNonNull(start, "start must not be null");
		Objects.requireNonNull(end, "end must not be null");
		Objects.requireNonNull(timezone, "timezone must not be null");
		Objects.requireNonNull(resultZone, "resultZone must not be null");

		if (start.isAfter(end)) {
			throw new IllegalArgumentException(String.format("start-date '%s' must not be past end-date '%s'", start, end));
		}

		var startDatetime = ZonedDateTime.of(start, LocalTime.MIN, timezone);
		var endDatetime = ZonedDateTime.of(end, LocalTime.MAX, timezone);

		var result = new ArrayList<EventViewDTO>();
		result.addAll(findOneTimeEvents(calendarKey, startDatetime, endDatetime, resultZone));
		result.addAll(findOneTimeReferencedEvents(calendarKey, startDatetime, endDatetime, resultZone));

		result.addAll(findMovedSeriesEvents(calendarKey, startDatetime, endDatetime, resultZone));
		result.addAll(findMovedSeriesReferencedEvents(calendarKey, startDatetime, endDatetime, resultZone));

		result.addAll(findSeriesEvents(calendarKey, startDatetime, endDatetime, resultZone));
		result.addAll(findSeriesReferencedEvents(calendarKey, startDatetime, endDatetime, resultZone));

		Collections.sort(result);

		return result;
	}

	private List<EventViewDTO> findOneTimeEvents(UUID calendarKey, ZonedDateTime startDatetime, ZonedDateTime endDatetime,
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
				.map(event -> EventViewDTO.of(event, resultZone))
				.toList();
	}

	private List<EventViewDTO> findOneTimeReferencedEvents(UUID calendarKey, ZonedDateTime startDatetime,
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
				.map(eventReference -> EventViewDTO.of(eventReference.event, resultZone))
				.toList();
	}

	private List<EventViewDTO> findMovedSeriesEvents(UUID calendarKey, ZonedDateTime startDatetime,
			ZonedDateTime endDatetime, ZoneId resultZone) {
		var query = em().createQuery("""
					FROM
						EventModificationMoved em
					JOIN FETCH em.event
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
				.map(eventModification -> EventViewDTO.of(eventModification, resultZone))
				.toList();
	}

	private List<EventViewDTO> findMovedSeriesReferencedEvents(UUID calendarKey, ZonedDateTime startDatetime,
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

		return query.getResultList()
				.stream()
				.map(eventModification -> EventViewDTO.of(eventModification, resultZone))
				.toList();
	}

	private List<EventViewDTO> findSeriesEvents(UUID calendarKey, ZonedDateTime startDatetime, ZonedDateTime endDatetime,
			ZoneId resultZone) {
		var query = em().createQuery("""
					FROM
							Event e
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

		return query.getResultList().stream().flatMap(event -> fromRepeat(event, startDatetime, endDatetime, resultZone))
				.toList();
	}

	private List<EventViewDTO> findSeriesReferencedEvents(UUID calendarKey, ZonedDateTime startDatetime,
			ZonedDateTime endDatetime, ZoneId resultZone) {
		var query = em().createQuery("""
					FROM
						EventReference er
					JOIN FETCH er.event e
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
				.flatMap(eventReference -> fromRepeat(eventReference.event, startDatetime, endDatetime, resultZone)).toList();
	}

	private static Stream<EventViewDTO> fromRepeat(EventEntity entity, ZonedDateTime startDatetime,
			ZonedDateTime endDatetime, ZoneId resultZone) {
				return RepeatUtils.fromRepeat(entity, startDatetime, endDatetime)
					.map( date -> EventViewDTO.of(entity, date, resultZone))
					.filter(Objects::nonNull);
	}
}
