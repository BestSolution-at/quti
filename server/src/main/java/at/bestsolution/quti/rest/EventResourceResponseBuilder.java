package at.bestsolution.quti.rest;

import at.bestsolution.quti.rest.dto.EventDTOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Singleton
public class EventResourceResponseBuilder {
	public ResponseBuilder get(EventDTOImpl result) {
		return Response.status(200).entity(result);
	}

	public ResponseBuilder create(String calendarKey, String result) {
		return Response.status(201).entity(result);
	}

	public ResponseBuilder update() {
		return Response.status(200);
	}

	public ResponseBuilder delete() {
		return Response.status(204);
	}

	public ResponseBuilder endRepeat() {
		return Response.status(204);
	}

	public ResponseBuilder move() {
		return Response.status(204);
	}

	public ResponseBuilder cancel() {
		return Response.status(204);
	}

	public ResponseBuilder uncancel() {
		return Response.status(204);
	}

	public ResponseBuilder description() {
		return Response.status(204);
	}
}
