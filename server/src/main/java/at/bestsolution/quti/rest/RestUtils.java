package at.bestsolution.quti.rest;

import at.bestsolution.quti.rest.model._JsonUtils;
import at.bestsolution.quti.service._RSDException;
import jakarta.ws.rs.core.Response;

public class RestUtils {

	public static Response toResponse(int status, _RSDException e) {
		if (e instanceof _RSDException.RSDMessageException m) {
			return Response.status(status).entity(m.message).build();
		} else if (e instanceof _RSDException.RSDStructuredDataException s) {
			return Response.status(status).entity(_JsonUtils.toJsonString(s.data, false)).build();
		}
		return Response.serverError().build();
	}
}
