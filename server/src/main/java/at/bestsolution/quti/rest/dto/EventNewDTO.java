// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record EventNewDTO(
    String title,
    String description,
    ZonedDateTime start,
    ZonedDateTime end,
    boolean fullday,
    EventRepeatDTO repeat,
    List<String> tags,
    List<String> referencedCalendars) {
}