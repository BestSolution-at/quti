// Generated by RSD - Do not modify
package at.bestsolution.quti.rest;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

import at.bestsolution.quti.rest.model._JsonUtils;
import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventNew;

@Singleton
public class EventResourceResponseBuilder {
	public ResponseBuilder create(String result, String calendar, EventNew.Data event) {
		return Response.status(201).entity(_JsonUtils.encodeAsJsonString(result));
	}

	public ResponseBuilder get(Event.Data result, String calendar, String key, ZoneId timezone) {
		return Response.status(200).entity(_JsonUtils.toJsonString(result, false));
	}

	public ResponseBuilder update(String calendar, String key, Event.Patch changes) {
		return Response.status(204);
	}

	public ResponseBuilder delete(String calendar, String key) {
		return Response.status(204);
	}

	public ResponseBuilder cancel(String calendar, String key) {
		return Response.status(204);
	}

	public ResponseBuilder uncancel(String calendar, String key) {
		return Response.status(204);
	}

	public ResponseBuilder move(String calendar, String key, ZonedDateTime start, ZonedDateTime end) {
		return Response.status(204);
	}

	public ResponseBuilder endRepeat(String calendar, String key, LocalDate end) {
		return Response.status(204);
	}

	public ResponseBuilder description(String calendar, String key, String description) {
		return Response.status(204);
	}

}
