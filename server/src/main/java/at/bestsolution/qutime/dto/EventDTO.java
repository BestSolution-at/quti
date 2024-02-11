package at.bestsolution.qutime.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.qutime.model.EventEntity;

public record EventDTO(
		String key,
		String title,
		String description,
		ZonedDateTime start,
		ZonedDateTime end,
		EventRepeatDTO repeat) {

	public static EventDTO of(EventEntity event, ZoneId zoneId) {
		return new EventDTO(
				event.key.toString(),
				event.title,
				event.desription,
				event.start.withZoneSameInstant(zoneId),
				event.end.withZoneSameInstant(zoneId), EventRepeatDTO.of(event.repeatPattern));
	}
}
