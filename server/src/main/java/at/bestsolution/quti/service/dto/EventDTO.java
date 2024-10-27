// Generated by RSD - Do not modify
package at.bestsolution.quti.service.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.List;

public interface EventDTO extends BaseDTO {
    /**
     * Unique identifier of the event
     */
    public String key();
    /**
     * basic description
     */
    public String title();
    /**
     * a more detailed description
     */
    public String description();
    /**
     * start time
     */
    public ZonedDateTime start();
    /**
     * end time
     */
    public ZonedDateTime end();
    /**
     * mark it as a fullday event
     */
    public boolean fullday();
    /**
     * the repeat pattern
     */
    public EventRepeatDTO repeat();
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
        public Builder title(String title);
        public Builder description(String description);
        public Builder start(ZonedDateTime start);
        public Builder end(ZonedDateTime end);
        public Builder fullday(boolean fullday);
        public Builder repeat(EventRepeatDTO repeat);
        public Builder tags(List<String> tags);
        public Builder referencedCalendars(List<String> referencedCalendars);
        public <T extends EventRepeatDTO.Builder> Builder withRepeat(Class<T> clazz, Function<T, EventRepeatDTO> block);
        public EventDTO build();
    }
}
