package at.bestsolution.quti.service.jpa.event.utils;

import java.time.ZoneId;

import at.bestsolution.quti.model.EventEntity;
import at.bestsolution.quti.service.DTOBuilderFactory;
import at.bestsolution.quti.service.dto.EventDTO;

public class EventDTOUtil {
	public static EventDTO of(DTOBuilderFactory factory, EventEntity event, ZoneId zoneId) {
		var b = factory.builder(EventDTO.Builder.class);
		return b
			.key(event.key.toString())
			.title(event.title)
			.description(event.description)
			.start(event.start.withZoneSameInstant(zoneId))
			.end(event.end.withZoneSameInstant(zoneId))
			.fullday(event.fullday)
			.repeat(EventRepeatDTOUtil.of(factory, event.repeatPattern))
			.tags(event.tags)
			.referencedCalendars(event.references.stream().map( e -> e.calendar.key.toString()).toList())
			.build();
	}
}
