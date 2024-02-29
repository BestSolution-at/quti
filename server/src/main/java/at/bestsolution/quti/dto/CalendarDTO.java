package at.bestsolution.quti.dto;

import at.bestsolution.quti.model.CalendarEntity;

public record CalendarDTO(String key, String name, String owner) {
	public static CalendarDTO of(CalendarEntity entity) {
		return new CalendarDTO(entity.key.toString(), entity.name, entity.owner);
	}
}
