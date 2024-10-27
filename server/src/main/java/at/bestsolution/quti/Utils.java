package at.bestsolution.quti;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.UUID;

import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.Result.ResultType;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonString;
import jakarta.json.JsonValue.ValueType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class Utils {

	@FunctionalInterface
	public interface JsonPatchOperationHandler<T> {
		public Result<Void> apply(T entity, JsonObject o, List<Runnable> runnable);
	}

	public static ZonedDateTime atStartOfDay(ZonedDateTime datetime) {
		return datetime.with(ChronoField.HOUR_OF_DAY, 0);
	}

	public static ZonedDateTime atEndOfDay(ZonedDateTime datetime) {
		return datetime.toLocalDateTime().with(LocalTime.parse("23:59:59")).atZone(datetime.getZone());
	}

	public static String getAsString(String name, JsonObject o) {
		var value = o.get(name);
		if( value.getValueType() == ValueType.STRING ) {
			return ((JsonString)o).getString();
		}
		return null;
	}

	public static String getPatchStringValue(JsonObject o) {
		return getAsString("value", o);
	}

	public static void throwAsException(Result<?> result) {
		if( result.isOk() ) {
			return;
		}
		if( result.type() == ResultType.INVALID_CONTENT ) {
			throw new WebApplicationException(result.message(), Status.NOT_FOUND);
		} else if( result.type() == ResultType.NOT_FOUND || result.type() == ResultType.INVALID_PARAMETER ) {
			throw new WebApplicationException(result.message(), Status.BAD_REQUEST);
		}
	}

	public static Response toResponse(Result<?> result) {
		if (result.type() == ResultType.NOT_FOUND) {
			return notFound(result.message());
		} else if (result.type() == ResultType.INVALID_CONTENT || result.type() == ResultType.INVALID_PARAMETER) {
			return badRequest(result.message());
		}
		throw new IllegalStateException(String.format("Unable to convert %s to a response", result.type()));
	}

	public static Result<LocalDate> parseLocalDate(String date, String paramName) {
		try {
			return Result.ok(LocalDate.parse(date));
		} catch (Throwable t) {
			return Result.invalidParameter("'%s' in %s is not a valid ISO-Date", date, paramName);
		}
	}

	public static Result<ZoneId> parseZone(String zone, String paramName) {
		try {
			return Result.ok(ZoneId.of(zone));
		} catch (Throwable t) {
			return Result.invalidParameter("'%s' in %s is not a valid timezone", zone, paramName);
		}
	}

	public static Result<UUID> parseUUID(String uuid, String paramName) {
		try {
			return Result.ok(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return Result.invalidParameter("'%s' in %s is not a valid UUID", uuid, paramName);
		}
	}

	public static Result<JsonPatch> parseJsonPatch(String jsonPatch, String paramName) {
		try {
			var patchArray = Json.createReader(new StringReader(jsonPatch)).readArray();
			return Result.ok(Json.createPatchBuilder(patchArray).build());
		} catch (Throwable t) {
			return Result.invalidContent("Provided data in '%s' is not a JSON-Patch", paramName);
		}
	}

	public static Response notFound(String errorText, Object... data) {
		return createErrorResponse(Status.NOT_FOUND.getStatusCode(), errorText);
	}

	public static Response unprocessableContent(String errorText, Object... data) {
		return createErrorResponse(Status.BAD_REQUEST.getStatusCode(), String.format(errorText, data));
	}

	public static Response badRequest(String errorText, Object... data) {
		return createErrorResponse(Status.BAD_REQUEST.getStatusCode(), String.format(errorText, data));
	}

	public static Response createErrorResponse(int status, String errorText) {
		return Response.status(status, errorText).build();
	}
}
