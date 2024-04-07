package at.bestsolution.quti;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import at.bestsolution.quti.Utils.ParseResult;
import at.bestsolution.quti.dto.EventMoveDTO;
import at.bestsolution.quti.dto.EventNewDTO;
import at.bestsolution.quti.handler.event.CancelHandler;
import at.bestsolution.quti.handler.event.CreateHandler;
import at.bestsolution.quti.handler.event.DeleteHandler;
import at.bestsolution.quti.handler.event.GetHandler;
import at.bestsolution.quti.handler.event.MoveHandler;
import at.bestsolution.quti.handler.event.UncancelHandler;
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

	private final GetHandler getHandler;
	private final CreateHandler createHandler;
	private final DeleteHandler deleteHandler;
	private final MoveHandler moveHandler;
	private final CancelHandler cancelHandler;
	private final UncancelHandler uncancelHandler;

	@Inject
	public EventResource(GetHandler getHandler,
		CreateHandler createHandler,
		DeleteHandler deleteHandler,
		MoveHandler moveHandler,
		CancelHandler cancelHandler,
		UncancelHandler uncancelHandler) {
		this.getHandler = getHandler;
		this.createHandler = createHandler;
		this.deleteHandler = deleteHandler;
		this.moveHandler = moveHandler;
		this.cancelHandler = cancelHandler;
		this.uncancelHandler = uncancelHandler;
	}

	@GET
	@Path("{key}")
	public Response get(
			@PathParam("calendar") String calendarKey,
			@PathParam("key") String eventKey,
			@HeaderParam("timezone") String zone) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

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

	@PATCH
	public Response update(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, String patch) {
		return Response.ok().build();
	}

	@Path("{key}")
	@DELETE
	public Response delete(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

		if( parsedCalendarKey.response() != null ) {
			return parsedCalendarKey.response();
		}

		if( parsedEventKey.response() != null ) {
			return parsedEventKey.response();
		}

		var result = deleteHandler.delete(parsedCalendarKey.value(), parsedEventKey.value());
		if( result.isOk() ) {
			return Response.noContent().build();
		}
		return Utils.toResponse(result);
	}

	@Path("{key}/action/move")
	@PUT
	public Response move(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, EventMoveDTO dto) {
		var seriesSep = eventKey.indexOf('_');
		if( seriesSep == -1 ) {
			return Utils.badRequest("'%s' is not an event in a series", eventKey);
		}

		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey.substring(0,seriesSep), "in path");
		var parsedOriginalDate = Utils.parseLocalDate(eventKey.substring(seriesSep+1), "in path");

		if( parsedCalendarKey.response() != null ) {
			return parsedCalendarKey.response();
		}
		if( parsedEventKey.response() != null ) {
			return parsedEventKey.response();
		}
		if( parsedOriginalDate.response() != null ) {
			return parsedOriginalDate.response();
		}

		var result = moveHandler.move(parsedCalendarKey.value(), parsedEventKey.value(), parsedOriginalDate.value(), dto.start(), dto.end());

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}

	@Path("{key}/action/cancel")
	@PUT
	public Response cancel(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var seriesSep = eventKey.indexOf('_');

		var parsedEventKey = seriesSep == -1 ? Utils.parseUUID(eventKey, "in path") : Utils.parseUUID(eventKey.substring(0,seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? new ParseResult<LocalDate>(null, null) : Utils.parseLocalDate(eventKey.substring(seriesSep+1), "in path");
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");

		if( parsedCalendarKey.response() != null ) {
			return parsedCalendarKey.response();
		}
		if( parsedEventKey.response() != null ) {
			return parsedEventKey.response();
		}
		if( parsedOriginalDate.response() != null ) {
			return parsedOriginalDate.response();
		}

		var result = cancelHandler.cancel(parsedCalendarKey.value(), parsedEventKey.value(), parsedOriginalDate.value());

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}

	@Path("{key}/action/uncancel")
	@PUT
	public Response uncancel(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var seriesSep = eventKey.indexOf('_');

		var parsedEventKey = seriesSep == -1 ? Utils.parseUUID(eventKey, "in path") : Utils.parseUUID(eventKey.substring(0,seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? new ParseResult<LocalDate>(null, null) : Utils.parseLocalDate(eventKey.substring(seriesSep+1), "in path");
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		if( parsedCalendarKey.response() != null ) {
			return parsedCalendarKey.response();
		}
		if( parsedEventKey.response() != null ) {
			return parsedEventKey.response();
		}
		if( parsedOriginalDate.response() != null ) {
			return parsedOriginalDate.response();
		}

		var result = uncancelHandler.uncancel(parsedCalendarKey.value(), parsedEventKey.value(), parsedOriginalDate.value());

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}
}
