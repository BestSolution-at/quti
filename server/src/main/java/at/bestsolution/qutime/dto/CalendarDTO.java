package at.bestsolution.qutime.dto;

import at.bestsolution.qutime.model.CalendarEntity;

public record CalendarDTO(String key, String name, String owner) {
	public static CalendarDTO of(CalendarEntity entity) {
		return new CalendarDTO(entity.key.toString(), entity.name, entity.owner);
	}
}
