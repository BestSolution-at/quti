package at.bestsolution.quti.rest;

import at.bestsolution.quti.rest.model._JsonUtils;
import at.bestsolution.quti.service.RSDException;
import jakarta.ws.rs.core.Response;

public class RestUtils {

	public static Response toResponse(int status, RSDException e) {
		if (e instanceof RSDException.RSDMessageException m) {
			return Response.status(status).header("X-RSD-Error-Type", e.type).entity(_JsonUtils.encodeAsJsonString(m.message))
					.build();
		} else if (e instanceof RSDException.RSDStructuredDataException s) {
			return Response.status(status).header("X-RSD-Error-Type", e.type).entity(_JsonUtils.toJsonString(s.data, false))
					.build();
		}
		return Response.serverError().entity("Unknown error '%'".formatted(e.getClass().getName())).build();
	}
}
