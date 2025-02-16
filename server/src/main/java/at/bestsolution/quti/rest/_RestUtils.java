// Generated by RSD - Do not modify
package at.bestsolution.quti.rest;

import jakarta.ws.rs.core.Response;

import at.bestsolution.quti.rest.model._JsonUtils;
import at.bestsolution.quti.service.RSDException;

public class _RestUtils {
	public static Response toResponse(int status, RSDException e) {
		if (e instanceof RSDException.RSDStructuredDataException s) {
			return Response.status(status)
					.header("X-RSD-Error-Type", e.type)
					.header("X-RSD-Error-Message", e.getMessage())
					.entity(_JsonUtils.toJsonString(s.data, false)).build();
		}
		return Response.status(status)
				.header("X-RSD-Error-Type", e.type)
				.header("X-RSD-Error-Message", e.getMessage())
				.entity(_JsonUtils.encodeAsJsonString(e.getMessage())).build();
	}
}
