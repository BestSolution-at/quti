package at.bestsolution.quti.calendar.service.jpa.event.utils;

import java.time.ZoneId;

import at.bestsolution.quti.calendar.service.jpa.model.EventEntity;
import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.model.Event;

public class EventDTOUtil {
	public static Event.Data of(BuilderFactory factory, EventEntity event, ZoneId zoneId) {
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
