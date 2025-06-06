package at.bestsolution.quti.calendar.service.jpa.calendar.utils;

import at.bestsolution.quti.calendar.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.model.Calendar;

public class CalendarDTOUtil {
	public static Calendar.Data of(BuilderFactory factory, CalendarEntity entity) {
		var b = factory.builder(Calendar.DataBuilder.class);
		return b
				.key(entity.key.toString())
				.name(entity.name)
				.owner(entity.owner)
				.build();
	}
}
