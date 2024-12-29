// Generated by RSD - Do not modify
package at.bestsolution.quti.service.dto;

import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.service.dto.MixinEventViewDataDTO.Status;

public interface EventViewDTO extends BaseDTO {
    /**
     * unique identifier of the event
     */
    public String key();
    /**
     * the calendar the event is located in
     */
    public String calendarKey();
    /**
     * basic description
     */
    public String title();
    /**
     * a more detailed description
     */
    public String description();
    /**
     * owner of the event
     */
    public String owner();
    /**
     * event status
     */
    public Status status();
    /**
     * start time
     */
    public ZonedDateTime start();
    /**
     * end time
     */
    public ZonedDateTime end();
    /**
     * a list of custom tags
     */
    public List<String> tags();
    /**
     * other calendars this event is referenced in
     */
    public List<String> referencedCalendars();

    public interface Builder extends BaseDTO.Builder {
        public Builder key(String key);
        public Builder calendarKey(String calendarKey);
        public Builder title(String title);
        public Builder description(String description);
        public Builder owner(String owner);
        public Builder status(Status status);
        public Builder start(ZonedDateTime start);
        public Builder end(ZonedDateTime end);
        public Builder tags(List<String> tags);
        public Builder referencedCalendars(List<String> referencedCalendars);
        public EventViewDTO build();
    }

    public interface SingleEventViewDTO extends EventViewDTO, MixinEventViewDataDTO {

        public interface Builder extends EventViewDTO.Builder, MixinEventViewDataDTO.Builder {
            public Builder key(String key);
            public Builder calendarKey(String calendarKey);
            public Builder title(String title);
            public Builder description(String description);
            public Builder owner(String owner);
            public Builder status(Status status);
            public Builder start(ZonedDateTime start);
            public Builder end(ZonedDateTime end);
            public Builder tags(List<String> tags);
            public Builder referencedCalendars(List<String> referencedCalendars);
            public SingleEventViewDTO build();
        }
    }

    public interface SeriesMovedEventViewDTO extends EventViewDTO, MixinEventViewDataDTO {
        /**
         * key of the original event
         */
        public String masterEventKey();
        /**
         * the original start
         */
        public ZonedDateTime originalStart();
        /**
         * the original end
         */
        public ZonedDateTime originalEnd();

        public interface Builder extends EventViewDTO.Builder, MixinEventViewDataDTO.Builder {
            public Builder masterEventKey(String masterEventKey);
            public Builder originalStart(ZonedDateTime originalStart);
            public Builder originalEnd(ZonedDateTime originalEnd);
            public Builder key(String key);
            public Builder calendarKey(String calendarKey);
            public Builder title(String title);
            public Builder description(String description);
            public Builder owner(String owner);
            public Builder status(Status status);
            public Builder start(ZonedDateTime start);
            public Builder end(ZonedDateTime end);
            public Builder tags(List<String> tags);
            public Builder referencedCalendars(List<String> referencedCalendars);
            public SeriesMovedEventViewDTO build();
        }
    }

    public interface SeriesEventViewDTO extends EventViewDTO, MixinEventViewDataDTO {
        /**
         * key of the original event
         */
        public String masterEventKey();

        public interface Builder extends EventViewDTO.Builder, MixinEventViewDataDTO.Builder {
            public Builder masterEventKey(String masterEventKey);
            public Builder key(String key);
            public Builder calendarKey(String calendarKey);
            public Builder title(String title);
            public Builder description(String description);
            public Builder owner(String owner);
            public Builder status(Status status);
            public Builder start(ZonedDateTime start);
            public Builder end(ZonedDateTime end);
            public Builder tags(List<String> tags);
            public Builder referencedCalendars(List<String> referencedCalendars);
            public SeriesEventViewDTO build();
        }
    }
}
