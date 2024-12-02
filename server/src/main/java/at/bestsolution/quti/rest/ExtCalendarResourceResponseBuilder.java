package at.bestsolution.quti.rest;

import java.net.URI;

import at.bestsolution.quti.rest.dto.CalendarNewDTOImpl;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Singleton
@Alternative
@Priority(1)
public class ExtCalendarResourceResponseBuilder extends CalendarResourceResponseBuilder {

	@Override
	public ResponseBuilder create(String result, CalendarNewDTOImpl calendar) {
		return super.create(result, calendar)
			.location(URI.create("/api/calendar/" + result));
	}
}
