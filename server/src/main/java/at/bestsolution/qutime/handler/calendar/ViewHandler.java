package at.bestsolution.qutime.handler.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import at.bestsolution.qutime.dto.EventViewDTO;
import at.bestsolution.qutime.model.EventEntity;
import at.bestsolution.qutime.model.EventReferenceEntity;
import at.bestsolution.qutime.model.EventRepeatEntity;
import at.bestsolution.qutime.model.modification.EventModificationMovedEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatRelativeYearlyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatWeeklyEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class ViewHandler {
    @Inject
    EntityManager em;

    public List<EventViewDTO> view(UUID calendarKey, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultZone) {
        Objects.requireNonNull(calendarKey);
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        Objects.requireNonNull(timezone);
        Objects.requireNonNull(resultZone);
        
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

    private List<EventViewDTO> findOneTimeEvents(UUID calendarKey, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId resultZone) {
        var query = em.createQuery("""
            FROM 
                Event e 
            WHERE 
                e.calendar.key = :calendarKey 
            AND e.repeatPattern IS NULL 
            AND ( 
                e.start BETWEEN :startDatetime AND :endDatetime
                OR
                e.end BETWEEN :startDatetime AND :endDatetime
            )
        """, EventEntity.class);
        query.setParameter("calendarKey", calendarKey);
        query.setParameter("startDatetime", startDatetime);
        query.setParameter("endDatetime", endDatetime);

        return query.getResultList()
            .stream()
            .map( event -> EventViewDTO.of(event, resultZone))
            .toList();
    }

    private List<EventViewDTO> findOneTimeReferencedEvents(UUID calendarKey, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId resultZone) {
        var query = em.createQuery("""
            FROM
                EventReference er
            JOIN FETCH er.event e
            WHERE
                er.calendar.key = :calendarKey
            AND e.repeatPattern IS NULL
            AND (
                e.start BETWEEN :startDatetime AND :endDatetime
                OR
                e.end BETWEEN :startDatetime AND :endDatetime
            )
        """, EventReferenceEntity.class);

        query.setParameter("calendarKey", calendarKey);
        query.setParameter("startDatetime", startDatetime);
        query.setParameter("endDatetime", endDatetime);

        return query.getResultList()
            .stream()
            .map( eventReference -> EventViewDTO.of(eventReference.event, resultZone))
            .toList();
    }

    private List<EventViewDTO> findMovedSeriesEvents(UUID calendarKey, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId resultZone) {
        var query = em.createQuery("""
            FROM
                EventModificationMoved em
            JOIN FETCH em.event
            WHERE
                em.event.calendar.key = :calendarKey
            AND ( 
                em.start BETWEEN :startDatetime AND :endDatetime
                OR
                em.end BETWEEN :startDatetime AND :endDatetime
            )
        """, EventModificationMovedEntity.class);
        query.setParameter("calendarKey", calendarKey);
        query.setParameter("startDatetime", startDatetime);
        query.setParameter("endDatetime", endDatetime);

        return query.getResultList()
            .stream()
            .map( eventModification -> EventViewDTO.of(eventModification, resultZone))
            .toList();
    }

    private List<EventViewDTO> findMovedSeriesReferencedEvents(UUID calendarKey, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId resultZone) {
        var query = em.createQuery("""
            FROM
                EventModificationMoved em
            JOIN FETCH em.event e
            JOIN FETCH em.event.references r
            WHERE
            ( 
                em.start BETWEEN :startDatetime AND :endDatetime
                OR
                em.end BETWEEN :startDatetime AND :endDatetime
            )    
            AND r.calendar.key = :calendarKey
            
        """, EventModificationMovedEntity.class);
        query.setParameter("calendarKey", calendarKey);
        query.setParameter("startDatetime", startDatetime);
        query.setParameter("endDatetime", endDatetime);

        return query.getResultList()
            .stream()
            .map( eventModification -> EventViewDTO.of(eventModification, resultZone))
            .toList();
    }

    private List<EventViewDTO> findSeriesEvents(UUID calendarKey, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId resultZone) {
        var query = em.createQuery("""
            FROM
                Event e
            JOIN FETCH e.repeatPattern
            WHERE
                e.calendar.key = :calendarKey
            AND ( 
                e.repeatPattern.startDate <= :startDate
                OR
                e.repeatPattern.startDate <= :endDate
            )
            AND ( 
                e.repeatPattern.endDate IS NULL 
                OR 
                e.repeatPattern.endDate >= :startDate
            )
        """, EventEntity.class);
        query.setParameter("calendarKey", calendarKey);
        query.setParameter("startDate", startDatetime);
        query.setParameter("endDate", endDatetime);

        return query.getResultList().stream().flatMap( event -> fromRepeat(event, startDatetime, endDatetime, resultZone)).toList();
    }

    private List<EventViewDTO> findSeriesReferencedEvents(UUID calendarKey, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId resultZone) {
        var query = em.createQuery("""
            FROM
                EventReference er
            JOIN FETCH er.event e
            JOIN FETCH e.repeatPattern
            WHERE
                er.calendar.key = :calendarKey
            AND ( 
                e.repeatPattern.startDate <= :startDate
                OR
                e.repeatPattern.startDate <= :endDate
            )
            AND ( 
                e.repeatPattern.endDate IS NULL 
                OR 
                e.repeatPattern.endDate >= :startDate
            )
        """, EventReferenceEntity.class);

        query.setParameter("calendarKey", calendarKey);
        query.setParameter("startDate", startDatetime);
        query.setParameter("endDate", endDatetime);

        return query.getResultList().stream().flatMap( eventReference -> fromRepeat(eventReference.event, startDatetime, endDatetime, resultZone)).toList();
    }

    private static ZonedDateTime boxEndDateTime(EventRepeatEntity entity, ZonedDateTime endDatetime) {
        if( entity.endDate == null ) {
            return endDatetime;
        } else {
            if( endDatetime.isBefore(entity.endDate) ) {
                return endDatetime;
            }
            return entity.endDate;
        }
    }

    private static ZonedDateTime boxStartDateTime(EventRepeatEntity entity, ZonedDateTime startDatetime) {
        if( startDatetime.isBefore(entity.startDate) ) {
            return entity.startDate;
        }
        return startDatetime;
    }

    private static EventViewDTO mapToView(EventEntity event, LocalDate date, ZoneId resultZone) {
        return EventViewDTO.of(event, date, resultZone);
    }

    private static Stream<EventViewDTO> fromRepeat(EventEntity entity, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId resultZone) {
        var boxStart = boxStartDateTime(entity.repeatPattern,startDatetime);
        var boxEnd = boxEndDateTime(entity.repeatPattern,endDatetime);

        if( entity.repeatPattern instanceof EventRepeatDailyEntity r ) {
            return fromRepeatDaily(r.interval, entity.start, boxStart, boxEnd)
                .map( date -> mapToView(entity, date, resultZone))
                .filter(Objects::nonNull);
        } else if( entity.repeatPattern instanceof EventRepeatWeeklyEntity r ) {
            return fromRepeatWeekly(r.daysOfWeek, r.interval, boxStart, boxEnd)
                .map( date -> mapToView(entity, date, resultZone))
                .filter(Objects::nonNull);
        } else if( entity.repeatPattern instanceof EventRepeatAbsoluteMonthlyEntity r ) {
            return fromRepeatAbsoluteMonthly(r, entity, boxStart, boxEnd, resultZone)
                .map( date -> mapToView(entity, date, resultZone))
                .filter(Objects::nonNull);
        } else if( entity.repeatPattern instanceof EventRepeatAbsoluteYearlyEntity r ) {
            return fromRepeatAbsoluteYearly(r, entity, boxStart, boxEnd, resultZone)
                .map( date -> mapToView(entity, date, resultZone))
                .filter(Objects::nonNull);
        } else if( entity.repeatPattern instanceof EventRepeatRelativeMonthlyEntity r ) {
            return fromRepeatRelativeMonthly(r, entity, boxStart, boxEnd, resultZone)
                .map( date -> mapToView(entity, date, resultZone))
                .filter(Objects::nonNull);
        } else if( entity.repeatPattern instanceof EventRepeatRelativeYearlyEntity r ) {
            return fromRepeatRelativeYearly(r, entity, boxStart, boxEnd, resultZone)
                .map( date -> mapToView(entity, date, resultZone))
                .filter(Objects::nonNull);
        }
        throw new IllegalStateException(String.format("Unknown repeatPattern '%s'", entity.repeatPattern));
    }

    public static Stream<LocalDate> fromRepeatDaily(int interval, ZonedDateTime eventStart, ZonedDateTime startDatetime, ZonedDateTime endDatetime) {
        List<LocalDate> dates = new ArrayList<>();

        var days = ChronoUnit.DAYS.between(eventStart.toLocalDate(), startDatetime.toLocalDate()) % interval;
        var currentDate = days == 0 ? startDatetime : startDatetime.plusDays(interval - days);
        
        while( currentDate.isBefore(endDatetime) ) {
            dates.add(currentDate.toLocalDate());
            currentDate = currentDate.plusDays(interval);
        }

        return dates.stream();
    }

    public static Stream<LocalDate> fromRepeatWeekly(List<DayOfWeek> daysOfWeek, int interval, ZonedDateTime startDatetime, ZonedDateTime endDatetime) {
        List<LocalDate> dates = new ArrayList<>();
        for( DayOfWeek day : daysOfWeek ) {
            var currentDate = startDatetime.with(TemporalAdjusters.nextOrSame(day));
        
            while( currentDate.isBefore(endDatetime) ) {
                dates.add(currentDate.toLocalDate());
                currentDate = currentDate.plusWeeks(interval);
            }
        }
        
        return dates.stream().sorted();
    }
    
    private static Stream<LocalDate> fromRepeatAbsoluteMonthly(EventRepeatAbsoluteMonthlyEntity repeat, EventEntity entity, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId zone) {
        return Stream.empty();
    }

    private static Stream<LocalDate> fromRepeatAbsoluteYearly(EventRepeatAbsoluteYearlyEntity repeat, EventEntity entity, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId zone) {
        return Stream.empty();
    }

    private static Stream<LocalDate> fromRepeatRelativeMonthly(EventRepeatRelativeMonthlyEntity repeat, EventEntity entity, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId zone) {
        return Stream.empty();
    }

    private static Stream<LocalDate> fromRepeatRelativeYearly(EventRepeatRelativeYearlyEntity repeat,EventEntity entity, ZonedDateTime startDatetime, ZonedDateTime endDatetime, ZoneId zone) {
        return Stream.empty();
    }        
}
