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
	private final RestDTOBuilderFactory builderFactory;
	private final EventService service;
	private final EventResourceResponseBuilder responseBuilder;

	@Inject
	public EventResource(
			EventService service,
			EventResourceResponseBuilder responseBuilder,
			RestDTOBuilderFactory builderFactory) {
		this.service = service;
		this.responseBuilder = responseBuilder;
		this.builderFactory = builderFactory;
	}

	@GET
	@Path("{key}")
	public Response get(
			@PathParam("calendar") String calendar,
			@PathParam("key") String key,
			@HeaderParam("timezone") ZoneId timezone) {
		var result = service.get(builderFactory, calendar, key, timezone);

		if (result.isOk()) {
			return responseBuilder.get(result.value(), calendar, key, timezone).build();
		}
		return RestUtils.toResponse(result);
	}

	@POST
	public Response create(
			@PathParam("calendar") String calendar,
			EventNewDTOImpl event) {
		var result = service.create(builderFactory, calendar, event);

		if (result.isOk()) {
			return responseBuilder.create(result.value(), calendar, event).build();
		}
		return RestUtils.toResponse(result);
	}

	@PATCH
	public Response update(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, String patch) {
		return responseBuilder.update(
				calendarKey,
				eventKey,
				patch).build();
	}

	@DELETE
	@Path("{key}")
	public Response delete(
			@PathParam("calendar") String calendar,
			@PathParam("key") String key) {
		var result = service.delete(builderFactory, calendar, key);

		if (result.isOk()) {
			return responseBuilder.delete(calendar, key).build();
		}
		return RestUtils.toResponse(result);
	}

	@PUT
	@Path("{key}/action/end-repeat")
	public Response endRepeat(
			@PathParam("calendar") String calendar,
			@PathParam("key") String key,
			LocalDate end) {
		var result = service.endRepeat(builderFactory, calendar, key, end);

		if (result.isOk()) {
			return responseBuilder.endRepeat(calendar, key, end).build();
		}
		return RestUtils.toResponse(result);
	}

	@PUT
	@Path("{key}/action/move")
	public Response move(
			@PathParam("calendar") String calendar,
			@PathParam("key") String key,
			EventMoveDTOImpl dto) {
		var result = service.move(builderFactory, calendar, key, dto.start(), dto.end());

		if (result.isOk()) {
			return responseBuilder.move(calendar, key, dto.start(), dto.end()).build();
		}
		return RestUtils.toResponse(result);
	}

	@PUT
	@Path("{key}/action/cancel")
	public Response cancel(
			@PathParam("calendar") String calendar,
			@PathParam("key") String key) {
		var result = service.cancel(builderFactory, calendar, key);

		if (result.isOk()) {
			return responseBuilder.cancel(calendar, key).build();
		}
		return RestUtils.toResponse(result);
	}

	@PUT
	@Path("{key}/action/uncancel")
	public Response uncancel(
			@PathParam("calendar") String calendar,
			@PathParam("key") String key) {
		var result = service.uncancel(builderFactory, calendar, key);

		if (result.isOk()) {
			return responseBuilder.uncancel(calendar, key).build();
		}
		return RestUtils.toResponse(result);
	}

	@PUT
	@Path("{key}/action/description")
	public Response description(
			@PathParam("calendar") String calendar,
			@PathParam("key") String key,
			String description) {
		var result = service.description(builderFactory, calendar, key, description);

		if (result.isOk()) {
			return responseBuilder.description(calendar, key, description).build();
		}
		return RestUtils.toResponse(result);
	}
}
