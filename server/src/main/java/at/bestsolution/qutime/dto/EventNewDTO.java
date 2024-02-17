package at.bestsolution.qutime.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record EventNewDTO(
		String title,
		String description,
		ZonedDateTime start,
		ZonedDateTime end,
		EventRepeatDTO repeat,
		List<String> referencedCalendars) {

}
