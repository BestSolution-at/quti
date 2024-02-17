package at.bestsolution.qutime.dto;

import java.time.ZonedDateTime;

public record EventNewDTO(
		String title,
		String description,
		ZonedDateTime start,
		ZonedDateTime end,
		EventRepeatDTO repeat) {

}
