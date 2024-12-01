package at.bestsolution.quti.rest;

import at.bestsolution.quti.service.dto.EventDTO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Singleton
public class EventResourceResponseBuilder {
	public ResponseBuilder get(EventDTO result) {
		return Response.status(200).entity(result);
	}

	public ResponseBuilder create(String result, String calendarKey) {
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
