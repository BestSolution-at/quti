package at.bestsolution.quti.rest;

import java.net.URI;

import at.bestsolution.quti.rest.dto.EventNewDTOImpl;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Singleton
@Alternative
@Priority(1)
public class ExtEventResourceResponseBuilder extends EventResourceResponseBuilder {
	@Override
	public ResponseBuilder create(String result, String calendarKey, EventNewDTOImpl event) {
		return super.create(result, calendarKey, event)
			.location(URI.create("/api/calendar/" + calendarKey + "/events/"+result));
	}
}
