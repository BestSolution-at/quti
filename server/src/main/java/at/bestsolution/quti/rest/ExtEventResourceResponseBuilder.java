package at.bestsolution.quti.rest;

import java.net.URI;

import jakarta.ws.rs.core.Response.ResponseBuilder;

public class ExtEventResourceResponseBuilder extends EventResourceResponseBuilder {
	@Override
	public ResponseBuilder create(String calendarKey, String result) {
		return super.create(calendarKey, result)
			.location(URI.create("/api/calendar/" + calendarKey + "/events/"+result));
	}
}
