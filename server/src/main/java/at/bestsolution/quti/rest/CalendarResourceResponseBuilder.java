package at.bestsolution.quti.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.rest.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.rest.dto.CalendarPatchDTOImpl;
import at.bestsolution.quti.service.dto.CalendarDTO;
import at.bestsolution.quti.service.dto.EventViewDTO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Singleton
public class CalendarResourceResponseBuilder {
	public ResponseBuilder get(CalendarDTO result, String key) {
		return Response.status(200).entity(result);
	}

	public ResponseBuilder create(String result, CalendarNewDTOImpl calendar) {
		return Response.status(201).entity(result);
	}

	public ResponseBuilder update(String key, CalendarPatchDTOImpl patch) {
		return Response.status(204);
	}

	public ResponseBuilder eventView(List<EventViewDTO> result, String key, LocalDate from, LocalDate to, ZoneId timezone, ZoneId resultTimeZone) {
		return Response.status(200).entity(result);
	}
}
