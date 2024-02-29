package at.bestsolution.quti.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.model.EventEntity;

public record EventDTO(
		String key,
		String title,
		String description,
		ZonedDateTime start,
		ZonedDateTime end,
		boolean fullday,
		List<String> tags,
		EventRepeatDTO repeat) {

	public static EventDTO of(EventEntity event, ZoneId zoneId) {
		return new EventDTO(
				event.key.toString(),
				event.title,
				event.description,
				event.start.withZoneSameInstant(zoneId),
				event.end.withZoneSameInstant(zoneId),
				event.fullday,
				event.tags,
				EventRepeatDTO.of(event.repeatPattern));
	}
}
