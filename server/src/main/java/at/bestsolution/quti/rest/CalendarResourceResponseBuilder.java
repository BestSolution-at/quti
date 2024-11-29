package at.bestsolution.quti.rest;

import java.util.List;

import at.bestsolution.quti.service.dto.CalendarDTO;
import at.bestsolution.quti.service.dto.EventViewDTO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Singleton
public class CalendarResourceResponseBuilder {
	public ResponseBuilder get(CalendarDTO result) {
		return Response.status(200).entity(result);
	}

	public ResponseBuilder create(String result) {
		return Response.status(201).entity(result);
	}

	public ResponseBuilder update() {
		return Response.status(204);
	}

	public ResponseBuilder views(List<EventViewDTO> result) {
		return Response.status(200).entity(result);
	}
}
