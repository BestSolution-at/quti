package at.bestsolution.qutime;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonPatch;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class Utils {
	public enum ResultType {
		OK,
		NOT_FOUND,
		INVALID_CONTENT
	}

	public record Result<T>(ResultType type, T value, String message) {

		public static Result<Void> OK = ok(null);

		public boolean isOk() {
			return type == ResultType.OK;
		}

		public static <T> Result<T> ok(T value) {
			return new Result<T>(ResultType.OK, value, null);
		}

		public static <T> Result<T> notFound(String message, Object... args) {
			return new Result<T>(ResultType.NOT_FOUND, null, String.format(message, args));
		}

		public static <T> Result<T> invalidContent(String message, Object... args) {
			return new Result<T>(ResultType.INVALID_CONTENT, null, String.format(message, args));
		}
	}

	public static Response toResponse(Result<?> result) {
		if (result.type == ResultType.NOT_FOUND) {
			return notFound(result.message);
		} else if (result.type == ResultType.INVALID_CONTENT) {
			return unprocessableContent(result.message);
		}
		throw new IllegalStateException(String.format("Unable to convert %s to a response", result.type));
	}

	public record ParseResult<T>(T value, Response response) {
		public static <T> ParseResult<T> success(T value) {
			return new ParseResult<>(value, null);
		}

		public static <T> ParseResult<T> fail(Response response) {
			return new ParseResult<T>(null, response);
		}
	}

	public static ParseResult<LocalDate> parseLocalDate(String date, String paramName) {
		try {
			return ParseResult.success(LocalDate.parse(date));
		} catch (Throwable t) {
			return ParseResult.fail(badRequest("'%s' in %s is not a valid ISO-Date", date, paramName));
		}
	}

	public static ParseResult<ZoneId> parseZone(String zone, String paramName) {
		try {
			return ParseResult.success(ZoneId.of(zone));
		} catch (Throwable t) {
			return ParseResult.fail(badRequest("'%s' in %s is not a valid timezone", zone, paramName));
		}
	}

	public static ParseResult<UUID> parseUUID(String uuid, String paramName) {
		try {
			return ParseResult.success(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return ParseResult.fail(badRequest("'%s' in %s is not a valid UUID", uuid, paramName));
		}
	}

	public static ParseResult<JsonPatch> parseJsonPatch(String jsonPatch, String paramName) {
		try {
			var patchArray = Json.createReader(new StringReader(jsonPatch)).readArray();
			return ParseResult.success(Json.createPatchBuilder(patchArray).build());
		} catch (Throwable t) {
			return ParseResult.fail(badRequest("Provided data in '%s' is not a JSON-Patch", paramName));
		}
	}

	public static Response notFound(String errorText, Object... data) {
		return createErrorResponse(Status.NOT_FOUND.getStatusCode(), errorText);
	}

	public static Response unprocessableContent(String errorText, Object... data) {
		return createErrorResponse(422, String.format(errorText, data));
	}

	public static Response badRequest(String errorText, Object... data) {
		return createErrorResponse(Status.BAD_REQUEST.getStatusCode(), String.format(errorText, data));
	}

	public static Response createErrorResponse(int status, String errorText) {
		return Response.status(status, errorText).build();
	}
}
