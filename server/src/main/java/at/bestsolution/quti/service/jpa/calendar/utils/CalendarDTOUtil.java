package at.bestsolution.quti.service.jpa.calendar.utils;

import at.bestsolution.quti.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.service.DTOBuilderFactory;
import at.bestsolution.quti.service.dto.CalendarDTO;

public class CalendarDTOUtil {
	public static CalendarDTO of(DTOBuilderFactory factory, CalendarEntity entity) {
		var b = factory.builder(CalendarDTO.Builder.class);
		return b
			.key(entity.key.toString())
			.name(entity.name)
			.owner(entity.owner)
			.build();
	}
}
