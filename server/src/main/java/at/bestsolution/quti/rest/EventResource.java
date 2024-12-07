package at.bestsolution.quti.rest;

import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.rest.dto.EventMoveDTOImpl;
import at.bestsolution.quti.rest.dto.EventNewDTOImpl;
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
	private final EventResourceResponseBuilder responseBuilder;

	@Inject
	public EventResource(EventService service, EventResourceResponseBuilder responseBuilder) {
		this.service = service;
		this.responseBuilder = responseBuilder;
	}

	@GET
	@Path("{key}")
	public Response get(
			@PathParam("calendar") String calendarKey,
			@PathParam("key") String eventKey,
			@HeaderParam("timezone") ZoneId zone) {

		var event = service.get(calendarKey, eventKey, zone);
		if (event.isOk()) {
			return responseBuilder.get(
				event.value(),
				calendarKey,
				eventKey,
				zone).build();
		}

		return RestUtils.toResponse(event);
	}


	@POST
	public Response create(@PathParam("calendar") String calendarKey, EventNewDTOImpl event) {
		var result = this.service.create(calendarKey, event);
		if( result.isOk() ) {
			return responseBuilder.create(
				result.value(),
				calendarKey,
				event).build();
		}
		return RestUtils.toResponse(result);
	}

	@PATCH
	public Response update(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, String patch) {
		return responseBuilder.update(
			calendarKey,
			eventKey,
			patch
		).build();
	}

	@Path("{key}")
	@DELETE
	public Response delete(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var result = service.delete(calendarKey, eventKey);
		if( result.isOk() ) {
			return responseBuilder.delete(
				calendarKey,
				eventKey
			).build();
		}
		return RestUtils.toResponse(result);
	}

	@Path("{key}/action/end-repeat")
	@PUT
	public Response endRepeat(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, LocalDate endDate) {
		var result = service.endRepeat(calendarKey, eventKey, endDate);
		if( result.isOk() ) {
			return responseBuilder.endRepeat(
				calendarKey,
				eventKey,
				endDate).build();
		}
		return RestUtils.toResponse(result);
	}

	@Path("{key}/action/move")
	@PUT
	public Response move(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, EventMoveDTOImpl dto) {
		var result = service.move(calendarKey, eventKey, dto.start(), dto.end());

		if( result.isOk() ) {
			return responseBuilder.move(
				calendarKey,
				eventKey,
				dto
			).build();
		}

		return RestUtils.toResponse(result);
	}

	@Path("{key}/action/cancel")
	@PUT
	public Response cancel(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var result = service.cancel(calendarKey, eventKey);

		if( result.isOk() ) {
			return responseBuilder.cancel(
				calendarKey,
				eventKey
			).build();
		}

		return RestUtils.toResponse(result);
	}

	@Path("{key}/action/uncancel")
	@PUT
	public Response uncancel(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var result = service.uncancel(calendarKey, eventKey);

		if( result.isOk() ) {
			return responseBuilder.uncancel(
				calendarKey,
				eventKey
			).build();
		}

		return RestUtils.toResponse(result);
	}

	@Path("{key}/action/description")
	@PUT
	public Response description(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, String description) {
		var result = service.description(calendarKey, eventKey, description);

		if( result.isOk() ) {
			return responseBuilder.description(
				calendarKey,
				eventKey,
				description
			).build();
		}

		return RestUtils.toResponse(result);
	}
}
