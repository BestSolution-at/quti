package at.bestsolution.qutime;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZoneOffset;

import at.bestsolution.qutime.dto.EventNewDTO;
import at.bestsolution.qutime.handler.event.CreateHandler;
import at.bestsolution.qutime.handler.event.GetHandler;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/api/calendar/{calendar}/events")
public class EventResource {

	private final GetHandler getHandler;

	private final CreateHandler createHandler;

	@Inject
	public EventResource(GetHandler getHandler, CreateHandler createHandler) {
		this.getHandler = getHandler;
		this.createHandler = createHandler;
	}

	@GET
	@Path("{key}")
	public Response get(
			@PathParam("calendar") String calendarKey,
			@PathParam("key") String eventKey,
			@HeaderParam("timezone") String zone) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in query parameter 'key'");

		if (parsedCalendarKey.response() != null) {
			return parsedCalendarKey.response();
		}
		if (parsedEventKey.response() != null) {
			return parsedEventKey.response();
		}

		var event = getHandler.get(parsedCalendarKey.value(), parsedEventKey.value(),
				zone == null ? ZoneOffset.UTC : ZoneId.of(zone));
		if (event == null) {
			return Utils.notFound(String.format("Could not find event with '%s'", eventKey));
		}

		return Response.ok(event).build();
	}


	@POST
	public Response create(@PathParam("calendar") String calendarKey, EventNewDTO event) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		if( parsedCalendarKey.response() != null ) {
			return parsedCalendarKey.response();
		}
		var result = this.createHandler.create(parsedCalendarKey.value(), event);
		if( result.isOk() ) {
			return Response.created(URI.create("/api/calendar/" + calendarKey + "/events/"+result.value())).build();
		}
		return Utils.toResponse(result);
	}
}
