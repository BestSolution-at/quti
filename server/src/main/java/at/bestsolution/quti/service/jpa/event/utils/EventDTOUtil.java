package at.bestsolution.quti.service.jpa.event.utils;

import java.time.ZoneId;

import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.DataBuilderFactory;
import at.bestsolution.quti.service.model.Event;

public class EventDTOUtil {
	public static Event.Data of(DataBuilderFactory factory, EventEntity event, ZoneId zoneId) {
		var b = factory.builder(Event.DataBuilder.class);
		return b
				.key(event.key.toString())
				.title(event.title)
				.description(event.description)
				.start(event.start.withZoneSameInstant(zoneId))
				.end(event.end.withZoneSameInstant(zoneId))
				.fullday(event.fullday)
				.repeat(EventRepeatDTOUtil.of(factory, event.repeatPattern))
				.tags(event.tags)
				.referencedCalendars(event.references.stream().map(e -> e.calendar.key.toString()).toList())
				.build();
	}
}
