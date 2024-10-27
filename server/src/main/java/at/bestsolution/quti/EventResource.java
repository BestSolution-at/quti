package at.bestsolution.quti;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.dto.EventMoveDTO;
import at.bestsolution.quti.dto.EventNewDTO;
import at.bestsolution.quti.service.EventService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/api/calendar/{calendar}/events")
public class EventResource {

	private final EventService service;

	@Inject
	public EventResource(EventService service) {
		this.service = service;
	}

	@GET
	@Path("{key}")
	public Response get(
			@PathParam("calendar") String calendarKey,
			@PathParam("key") String eventKey,
			@HeaderParam("timezone") ZoneId zone) {

		var event = service.get(calendarKey, eventKey, zone);
		if (event.isOk()) {
			return Response.ok(event.value()).build();
		}

		return Utils.toResponse(event);
	}


	@POST
	public Response create(@PathParam("calendar") String calendarKey, EventNewDTO event) {
		var result = this.service.create(calendarKey, event);
		if( result.isOk() ) {
			return Response.created(URI.create("/api/calendar/" + calendarKey + "/events/"+result.value())).entity(result.value()).build();
		}
		return Utils.toResponse(result);
	}

	@PATCH
	public Response update(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, String patch) {
		return Response.ok().build();
	}

	@Path("{key}")
	@DELETE
	public Response delete(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var result = service.delete(calendarKey, eventKey);
		if( result.isOk() ) {
			return Response.noContent().build();
		}
		return Utils.toResponse(result);
	}

	@Path("{key}/action/end-repeat")
	@PUT
	public Response endRepeat(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, LocalDate endDate) {
		var result = service.endRepeat(calendarKey, eventKey, endDate);
		if( result.isOk() ) {
			return Response.noContent().build();
		}
		return Utils.toResponse(result);
	}

	@Path("{key}/action/move")
	@PUT
	public Response move(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, EventMoveDTO dto) {
		var result = service.move(calendarKey, eventKey, dto.start(), dto.end());

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}

	@Path("{key}/action/cancel")
	@PUT
	public Response cancel(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var result = service.cancel(calendarKey, eventKey);

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}

	@Path("{key}/action/uncancel")
	@PUT
	public Response uncancel(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var result = service.uncancel(calendarKey, eventKey);

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}

	@Path("{key}/action/description")
	@PUT
	public Response description(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, String description) {
		var result = service.setDescription(calendarKey, eventKey, description);

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}
}
