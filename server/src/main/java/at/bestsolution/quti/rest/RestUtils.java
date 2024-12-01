package at.bestsolution.quti.rest;

import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.Result.ResultType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class RestUtils {
		public static Response toResponse(Result<?> result) {
		if (result.type() == ResultType.NOT_FOUND) {
			return notFound(result.message());
		} else if (result.type() == ResultType.INVALID_CONTENT || result.type() == ResultType.INVALID_PARAMETER) {
			return badRequest(result.message());
		}
		throw new IllegalStateException(String.format("Unable to convert %s to a response", result.type()));
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
