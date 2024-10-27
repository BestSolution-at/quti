// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

@JsonbTypeInfo({
    @JsonbSubtype(alias = "single", type = EventViewDTO.SingleEventViewDTO.class),
    @JsonbSubtype(alias = "series-moved", type = EventViewDTO.SeriesMovedEventViewDTO.class),
    @JsonbSubtype(alias = "series", type = EventViewDTO.SeriesEventViewDTO.class),
})
public abstract class EventViewDTO {
    public enum Status {
        ACCEPTED,
        CANCELED,
    }

    public String key;
    public String calendarKey;
    public String title;
    public String description;
    public String owner;
    public Status status;
    public ZonedDateTime start;
    public ZonedDateTime end;
    public List<String> tags;
    public List<String> referencedCalendars;

    public static class SingleEventViewDTO extends EventViewDTO {
    }

    public static class SeriesMovedEventViewDTO extends EventViewDTO {
        public String masterEventKey;
        public ZonedDateTime originalStart;
        public ZonedDateTime originalEnd;
    }

    public static class SeriesEventViewDTO extends EventViewDTO {
        public String masterEventKey;
    }
}