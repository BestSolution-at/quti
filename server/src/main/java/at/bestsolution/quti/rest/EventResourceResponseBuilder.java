package at.bestsolution.quti.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.rest.model._JsonUtils;
import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventNew;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Singleton
public class EventResourceResponseBuilder {
	public ResponseBuilder get(Event.Data result, String calendarKey, String eventKey, ZoneId zone) {
		return Response.status(200).entity(_JsonUtils.toJsonString(result, false));
	}

	public ResponseBuilder create(String result, String calendarKey, EventNew.Data event) {
		return Response.status(201).entity(result);
	}

	public ResponseBuilder update(String calendarKey, String eventKey, Event.Patch patch) {
		return Response.status(204);
	}

	public ResponseBuilder delete(String calendarKey, String eventKey) {
		return Response.status(204);
	}

	public ResponseBuilder endRepeat(String calendarKey, String eventKey, LocalDate endDate) {
		return Response.status(204);
	}

	public ResponseBuilder move(String calendarKey, String eventKey, ZonedDateTime start, ZonedDateTime end) {
		return Response.status(204);
	}

	public ResponseBuilder cancel(String calendarKey, String eventKey) {
		return Response.status(204);
	}

	public ResponseBuilder uncancel(String calendarKey, String eventKey) {
		return Response.status(204);
	}

	public ResponseBuilder description(String calendarKey, String eventKey, String description) {
		return Response.status(204);
	}
}
