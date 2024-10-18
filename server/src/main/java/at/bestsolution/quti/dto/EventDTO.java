package at.bestsolution.quti.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record EventDTO(
		String key,
		String title,
		String description,
		ZonedDateTime start,
		ZonedDateTime end,
		boolean fullday,
		List<String> tags,
		EventRepeatDTO repeat) {

}
