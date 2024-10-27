package at.bestsolution.quti.service.jpa.calendar.utils;

import at.bestsolution.quti.dto.CalendarDTO;
import at.bestsolution.quti.model.CalendarEntity;

public class CalendarDTOUtil {
	public static CalendarDTO of(CalendarEntity entity) {
		return new CalendarDTO(entity.key.toString(), entity.name, entity.owner);
	}
}
