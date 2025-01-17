package at.bestsolution.quti.service.jpa.calendar.utils;

import at.bestsolution.quti.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.service.DataBuilderFactory;
import at.bestsolution.quti.service.model.Calendar;

public class CalendarDTOUtil {
	public static Calendar.Data of(DataBuilderFactory factory, CalendarEntity entity) {
		var b = factory.builder(Calendar.DataBuilder.class);
		return b
				.key(entity.key.toString())
				.name(entity.name)
				.owner(entity.owner)
				.build();
	}
}
