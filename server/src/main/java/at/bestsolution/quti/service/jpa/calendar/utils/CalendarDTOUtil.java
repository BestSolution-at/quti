package at.bestsolution.quti.service.jpa.calendar.utils;

import at.bestsolution.quti.model.CalendarEntity;
import at.bestsolution.quti.rest.dto.CalendarDTOImpl;

public class CalendarDTOUtil {
	public static CalendarDTOImpl of(CalendarEntity entity) {
		return new CalendarDTOImpl(entity.key.toString(), entity.name, entity.owner);
	}
}
