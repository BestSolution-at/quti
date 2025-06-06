// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.rest;

import java.time.LocalDate;
import java.time.ZoneId;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;

import at.bestsolution.quti.calendar.rest.model._JsonUtils;
import at.bestsolution.quti.calendar.rest.model.EventMoveDataImpl;
import at.bestsolution.quti.calendar.service.EventService;
import at.bestsolution.quti.calendar.service.InvalidArgumentException;
import at.bestsolution.quti.calendar.service.model.Event;
import at.bestsolution.quti.calendar.service.model.EventNew;
import at.bestsolution.quti.calendar.service.model.EventSearch;
import at.bestsolution.quti.calendar.service.NotFoundException;

@ApplicationScoped
@Path("/api/calendar/{calendar}/events")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {
	private final RestBuilderFactory builderFactory;
	private final EventService service;
	private final EventResourceResponseBuilder responseBuilder;

	@Inject
	public EventResource(EventService service, EventResourceResponseBuilder responseBuilder, RestBuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
		this.service = service;
		this.responseBuilder = responseBuilder;
	}

	@POST
	public Response create(
			@PathParam("calendar") String _calendar,
			String _event) {
		var calendar = _calendar;
		var event = builderFactory.of(EventNew.Data.class, _event);
		try {
			var result = service.create(builderFactory, calendar, event);
			return responseBuilder.create(result, calendar, event).build();
		} catch (NotFoundException e) {
			return _RestUtils.toResponse(404, e);
		} catch (InvalidArgumentException e) {
			return _RestUtils.toResponse(400, e);
		}
	}

	@GET
	@Path("{key}")
	public Response get(
			@PathParam("calendar") String _calendar,
			@PathParam("key") String _key,
			@HeaderParam("timezone") String _timezone) {
		var calendar = _calendar;
		var key = _key;
		var timezone = _timezone == null ? null : ZoneId.of(_timezone);
		try {
			var result = service.get(builderFactory, calendar, key, timezone);
			return responseBuilder.get(result, calendar, key, timezone).build();
		} catch (NotFoundException e) {
			return _RestUtils.toResponse(404, e);
		} catch (InvalidArgumentException e) {
			return _RestUtils.toResponse(400, e);
		}
	}

	@PUT
	@Path("search")
	public Response search(
			@PathParam("calendar") String _calendar,
			String _filter,
			@HeaderParam("timezone") String _timezone) {
		var calendar = _calendar;
		var filter = builderFactory.of(EventSearch.Data.class, _filter);
		var timezone = _timezone == null ? null : ZoneId.of(_timezone);
		try {
			var result = service.search(builderFactory, calendar, filter, timezone);
			return responseBuilder.search(result, calendar, filter, timezone).build();
		} catch (InvalidArgumentException e) {
			return _RestUtils.toResponse(400, e);
		}
	}

	@PATCH
	@Path("{key}")
	public Response update(
			@PathParam("calendar") String _calendar,
			@PathParam("key") String _key,
			String _changes) {
		var calendar = _calendar;
		var key = _key;
		var changes = builderFactory.of(Event.Patch.class, _changes);
		try {
			service.update(builderFactory, calendar, key, changes);
			return responseBuilder.update(calendar, key, changes).build();
		} catch (NotFoundException e) {
			return _RestUtils.toResponse(404, e);
		} catch (InvalidArgumentException e) {
			return _RestUtils.toResponse(400, e);
		}
	}

	@DELETE
	@Path("{key}")
	public Response delete(
			@PathParam("calendar") String _calendar,
			@PathParam("key") String _key) {
		var calendar = _calendar;
		var key = _key;
		service.delete(builderFactory, calendar, key);
		return responseBuilder.delete(calendar, key).build();
	}

	@PUT
	@Path("{key}/action/cancel")
	public Response cancel(
			@PathParam("calendar") String _calendar,
			@PathParam("key") String _key) {
		var calendar = _calendar;
		var key = _key;
		service.cancel(builderFactory, calendar, key);
		return responseBuilder.cancel(calendar, key).build();
	}

	@PUT
	@Path("{key}/action/uncancel")
	public Response uncancel(
			@PathParam("calendar") String _calendar,
			@PathParam("key") String _key) {
		var calendar = _calendar;
		var key = _key;
		service.uncancel(builderFactory, calendar, key);
		return responseBuilder.uncancel(calendar, key).build();
	}

	@PUT
	@Path("{key}/action/move")
	public Response move(
			@PathParam("calendar") String _calendar,
			@PathParam("key") String _key,
			String data) {
		var calendar = _calendar;
		var key = _key;
		var dto = _JsonUtils.fromString(data, EventMoveDataImpl::new);
		service.move(builderFactory, calendar, key, dto.start(), dto.end());
		return responseBuilder.move(calendar, key, dto.start(), dto.end()).build();
	}

	@PUT
	@Path("{key}/action/end-repeat")
	public Response endRepeat(
			@PathParam("calendar") String _calendar,
			@PathParam("key") String _key,
			LocalDate _end) {
		var calendar = _calendar;
		var key = _key;
		var end = _end;
		service.endRepeat(builderFactory, calendar, key, end);
		return responseBuilder.endRepeat(calendar, key, end).build();
	}

	@PUT
	@Path("{key}/action/description")
	public Response description(
			@PathParam("calendar") String _calendar,
			@PathParam("key") String _key,
			String _description) {
		var calendar = _calendar;
		var key = _key;
		var description = _description;
		service.description(builderFactory, calendar, key, description);
		return responseBuilder.description(calendar, key, description).build();
	}

}
