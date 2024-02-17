package at.bestsolution.qutime.handler.event;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

import at.bestsolution.qutime.Utils;
import at.bestsolution.qutime.Utils.Result;
import at.bestsolution.qutime.handler.BaseHandler;
import at.bestsolution.qutime.model.EventEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonValue;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@Singleton
public class UpdateHandler extends BaseHandler {
	private static Map<String, BiFunction<EventEntity, JsonObject, Result<Runnable>>> REPLACE_OPS = new HashMap<>();

	static {
		REPLACE_OPS.put("title", UpdateHandler::handleTitleUpdate);
		REPLACE_OPS.put("description", UpdateHandler::handleDescriptionUpdate);
		REPLACE_OPS.put("start", UpdateHandler::handleStartUpdate);
		REPLACE_OPS.put("end", UpdateHandler::handleEndUpdate);
	}

	@Inject
	public UpdateHandler(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> update(UUID calendarKey, UUID eventKey, JsonPatch patch) {
		Objects.requireNonNull(calendarKey);
		Objects.requireNonNull(eventKey);
		Objects.requireNonNull(patch);

		var query = em().createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey", EventEntity.class);
		query.setParameter("eventKey", eventKey);
		query.setParameter("calendarKey", calendarKey);
		var result = query.getResultList();
		if( result.size() == 0 ) {
			return Result.notFound("Could not find event '%s' in calendar '%s'", eventKey, calendarKey);
		} else if( result.size() > 1 ) {
			throw new IllegalStateException(String.format("Multiple matching events for '%s' are found.", eventKey));
		}

		var entity = result.get(0);
		var updateRunnables = new ArrayList<Runnable>();

		for ( JsonValue e : patch.toJsonArray() ) {
			var op = e.asJsonObject();
			var operation = op.getString("op");
			var path = op.getString("path");

			if( "add".equals(operation) ) {

			} else if( "replace".equals(operation) ) {
				var handler = REPLACE_OPS.get(path);
				if( handler != null ) {
					var hrv = handler.apply(entity, op);
					if( hrv.isOk() ) {
						updateRunnables.add(hrv.value());
					} else {
						return hrv.toVoid();
					}
				} else {
					return Result.invalidContent("Operation '%s' on '%s' is not allowed", operation, path);
				}
			} else if( "remove".equals(operation) ) {

			}
		}

		updateRunnables.forEach(Runnable::run);

		if( entity.start.isAfter(entity.end) || entity.start.equals(entity.end) ) {
			throw new WebApplicationException("event.start has to be before event.end", 422);
		}

		var validate = EventUtils.validateEvent(entity);
		if( ! validate.isOk() ) {
			Utils.throwAsException(validate);
		}

		return Result.OK;
	}

	private static Result<Runnable> handleTitleUpdate(EventEntity entity, JsonObject op) {
		var value = Utils.getPatchStringValue(op);
		if( value == null ) {
			return Result.invalidContent("Title operation value must be a string");
		} else if( value.isBlank() ) {
			return Result.invalidContent("Title operation value must not be an empty string");
		}
		return Result.ok(() -> entity.title = value);
	}

	private static Result<Runnable> handleDescriptionUpdate(EventEntity entity, JsonObject op) {
		var value = Utils.getPatchStringValue(op);
		if( value == null ) {
			return Result.invalidContent("Description operation value must be a string");
		}
		return Result.ok(() -> entity.description = value);
	}

	private static Result<Runnable> handleStartUpdate(EventEntity entity, JsonObject op) {
		var value = Utils.getPatchStringValue(op);
		if( value == null ) {
			return Result.invalidContent("Start operation value must be a string encoding a date-time");
		}
		try {
			var start = ZonedDateTime.parse(value);
			return Result.ok(() -> {
				entity.start = start;
				if( entity.repeatPattern != null ) {
					entity.repeatPattern.startDate = ZonedDateTime.of(entity.start.toLocalDate(), LocalTime.MIN, entity.repeatPattern.recurrenceTimezone);
				}
			});
		} catch(DateTimeParseException e) {
			return Result.invalidContent("Start operation value '%s' is not a valid datetime with a timezone", op.getString("value"));
		}
	}

	private static Result<Runnable> handleEndUpdate(EventEntity entity, JsonObject op) {
		var value = Utils.getPatchStringValue(op);
		if( value == null ) {
			return Result.invalidContent("End operation value must be a string encoding a date-time");
		}
		try {
			var end = ZonedDateTime.parse(value);
			return Result.ok(() -> {
				entity.end = end;
			});
		} catch(DateTimeParseException e) {
			return Result.invalidContent("End operation value '%s' is not a valid datetime with a timezone", op.getString("value"));
		}
	}
}
