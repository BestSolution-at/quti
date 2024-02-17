package at.bestsolution.qutime.handler.event;

import at.bestsolution.qutime.model.EventEntity;
import at.bestsolution.qutime.Utils.Result;

public class EventUtils {
	public static Result<Void> validateEvent(EventEntity entity) {
		if( entity.start == null ) {
			return Result.invalidContent("event.start must not be null");
		}
		if( entity.end == null ) {
			return Result.invalidContent("event.end must not be null");
		}
		if( entity.title == null ) {
			return Result.invalidContent("event.title must not be null");
		}
		if( entity.title.isBlank() ) {
			return Result.invalidContent("event.title must not be blank");
		}
		if( entity.start.isAfter(entity.end) || entity.start.equals(entity.end) ) {
			return Result.invalidContent("event.start has to be before event.end");
		}
		return Result.OK;
	}
}
