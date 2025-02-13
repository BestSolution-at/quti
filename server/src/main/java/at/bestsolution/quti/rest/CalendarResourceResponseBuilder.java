package at.bestsolution.quti.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.rest.model._JsonUtils;
import at.bestsolution.quti.service.model.Calendar;
import at.bestsolution.quti.service.model.CalendarNew;
import at.bestsolution.quti.service.model.EventView;
//import at.bestsolution.quti.rest.model.CalendarPatchDTOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Singleton
public class CalendarResourceResponseBuilder {
	public ResponseBuilder get(Calendar.Data result, String key) {
		return Response.status(200)
				.entity(_JsonUtils.toJsonString(result, false));
	}

	public ResponseBuilder create(String result, CalendarNew.Data calendar) {
		return Response.status(201).entity(result);
	}

	public ResponseBuilder update(String key, Calendar.Patch patch) {
		return Response.status(204);
	}

	public ResponseBuilder eventView(List<EventView.Data> result, String key, LocalDate from, LocalDate to,
			ZoneId timezone,
			ZoneId resultTimeZone) {
		return Response.status(200).entity(_JsonUtils.toJsonString(result, false));
	}
}
