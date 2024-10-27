package at.bestsolution.quti.service.jpa.event.utils;

import java.time.ZoneId;

import at.bestsolution.quti.model.EventEntity;
import at.bestsolution.quti.rest.dto.EventDTO;

public class EventDTOUtil {
	public static EventDTO of(EventEntity event, ZoneId zoneId) {
		return new EventDTO(
				event.key.toString(),
				event.title,
				event.description,
				event.start.withZoneSameInstant(zoneId),
				event.end.withZoneSameInstant(zoneId),
				event.fullday,
				EventRepeatDTOUtil.of(event.repeatPattern),
				event.tags,
				event.references.stream().map( e -> e.calendar.key.toString()).toList()
			);
	}
}
