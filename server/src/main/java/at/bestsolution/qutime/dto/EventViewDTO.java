package at.bestsolution.qutime.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import at.bestsolution.qutime.model.EventEntity;
import at.bestsolution.qutime.model.EventModificationMovedEntity;

public record EventViewDTO(
    String key, 
    String title, 
    String description, 
    ZonedDateTime start, 
    ZonedDateTime end, 
    String masterCalendar,
    String masterKey,
    boolean series) implements Comparable<EventViewDTO> {
    public static EventViewDTO of(EventEntity event, ZoneId resultZone) {
        return new EventViewDTO(
            event.key.toString(),
            event.title, 
            event.desription,
            event.start.withZoneSameInstant(resultZone),
            event.end.withZoneSameInstant(resultZone),
            event.key.toString(),
            event.calendar.key.toString(),
            false);
    }

    @Override
    public int compareTo(EventViewDTO o) {
        var result = start().compareTo(o.start());
        if( result == 0 ) {
            result = end().compareTo(o.end());
            if( result == 0 ) {
                result = key().compareTo(o.key());
            }
        }
        
        return result;
    }

    public static EventViewDTO of(EventEntity event, LocalDate date, ZoneId recurrenceTimeZone, ZoneId zone) {
        var start = event.start.withZoneSameInstant(recurrenceTimeZone);
        var end = event.end.withZoneSameInstant(recurrenceTimeZone);
        var dayDiff = ChronoUnit.DAYS.between(start.toLocalDate(), date);
        
        var adjustedStart = start.plusDays(dayDiff);
        var adjustedEnd = end.plusDays(dayDiff);

        if( ! event.modifications.isEmpty() ) {
            var targetDate = adjustedStart.toLocalDate();
            var modified = event.modifications.stream()
                .map( m -> m.as(EventModificationMovedEntity.class))
                .filter(Objects::nonNull)
                .filter( m -> m.date.equals(targetDate))
                .findFirst().isPresent();
            if( modified ) {
                return null;
            }
        }

        return new EventViewDTO(
            event.key.toString()+"_"+date,
            event.title, 
            event.desription, 
            adjustedStart.withZoneSameInstant(zone), 
            adjustedEnd.withZoneSameInstant(zone), 
            event.key.toString(),
            event.calendar.key.toString(),
            true);
    }

    public static EventViewDTO of(EventModificationMovedEntity movedEntity, ZoneId zone) {
        return new EventViewDTO(
            movedEntity.event.key.toString() + "_" + movedEntity.date,
            movedEntity.event.title, 
            movedEntity.event.desription, 
            movedEntity.start.withZoneSameInstant(zone),
            movedEntity.end.withZoneSameInstant(zone), 
            movedEntity.event.key.toString(),
            movedEntity.event.calendar.key.toString(),
            true);
    }
}
