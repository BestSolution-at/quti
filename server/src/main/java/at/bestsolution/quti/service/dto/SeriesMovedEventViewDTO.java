// Generated by RSD - Do not modify
package at.bestsolution.quti.service.dto;

import java.time.ZonedDateTime;
import java.util.List;

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