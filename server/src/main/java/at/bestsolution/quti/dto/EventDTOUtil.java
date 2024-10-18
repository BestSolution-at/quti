package at.bestsolution.quti.dto;

import java.time.ZoneId;

import at.bestsolution.quti.model.EventEntity;

public class EventDTOUtil {
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
