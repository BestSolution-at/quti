// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

import at.bestsolution.quti.calendar.rest.model._JsonUtils;
import at.bestsolution.quti.calendar.service.model.Calendar;
import at.bestsolution.quti.calendar.service.model.CalendarNew;
import at.bestsolution.quti.calendar.service.model.EventView;

@Singleton
public class CalendarResourceResponseBuilder {
	public ResponseBuilder create(String result, CalendarNew.Data calendar) {
		return Response.status(201).entity(_JsonUtils.encodeAsJsonString(result));
	}

	public ResponseBuilder get(Calendar.Data result, String key) {
		return Response.status(200).entity(_JsonUtils.toJsonString(result, false));
	}

	public ResponseBuilder update(String key, Calendar.Patch changes) {
		return Response.status(204);
	}

	public ResponseBuilder eventView(List<EventView.Data> result, String key, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultTimeZone) {
		return Response.status(200).entity(_JsonUtils.toJsonString(result, false));
	}

}
