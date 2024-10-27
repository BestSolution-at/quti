package at.bestsolution.quti;

import java.net.URI;
import java.time.LocalDate;

import at.bestsolution.quti.Utils.Result;
import at.bestsolution.quti.dto.EventMoveDTO;
import at.bestsolution.quti.dto.EventNewDTO;
import at.bestsolution.quti.handler.event.CancelHandler;
import at.bestsolution.quti.handler.event.CreateHandler;
import at.bestsolution.quti.handler.event.DeleteHandler;
import at.bestsolution.quti.handler.event.EndRepeatingHandler;
import at.bestsolution.quti.handler.event.GetHandler;
import at.bestsolution.quti.handler.event.MoveHandler;
import at.bestsolution.quti.handler.event.SetDescriptionHandler;
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
	private final EndRepeatingHandler endRepeatHandler;
	private final SetDescriptionHandler setDescriptionHandler;

	@Inject
	public EventResource(GetHandler getHandler,
		CreateHandler createHandler,
		DeleteHandler deleteHandler,
		MoveHandler moveHandler,
		CancelHandler cancelHandler,
		UncancelHandler uncancelHandler,
		EndRepeatingHandler endRepeatHandler,
		SetDescriptionHandler setDescriptionHandler) {
		this.getHandler = getHandler;
		this.createHandler = createHandler;
		this.deleteHandler = deleteHandler;
		this.moveHandler = moveHandler;
		this.cancelHandler = cancelHandler;
		this.uncancelHandler = uncancelHandler;
		this.endRepeatHandler = endRepeatHandler;
		this.setDescriptionHandler = setDescriptionHandler;
	}

	@GET
	@Path("{key}")
	public Response get(
			@PathParam("calendar") String calendarKey,
			@PathParam("key") String eventKey,
			@HeaderParam("timezone") String zone) {

		var event = getHandler.get(calendarKey, eventKey, zone);
		if (event.isOk()) {
			return Response.ok(event.value()).build();
		}

		return Utils.toResponse(event);
	}


	@POST
	public Response create(@PathParam("calendar") String calendarKey, EventNewDTO event) {
		var result = this.createHandler.create(calendarKey, event);
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
		var result = deleteHandler.delete(calendarKey, eventKey);
		if( result.isOk() ) {
			return Response.noContent().build();
		}
		return Utils.toResponse(result);
	}

	@Path("{key}/action/end-repeat")
	@PUT
	public Response endRepeat(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, LocalDate endDate) {
		var result = endRepeatHandler.endRepeat(calendarKey, eventKey, endDate);
		if( result.isOk() ) {
			return Response.noContent().build();
		}
		return Utils.toResponse(result);
	}

	@Path("{key}/action/move")
	@PUT
	public Response move(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, EventMoveDTO dto) {
		var result = moveHandler.move(calendarKey, eventKey, dto.start(), dto.end());

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}

	@Path("{key}/action/cancel")
	@PUT
	public Response cancel(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var result = cancelHandler.cancel(calendarKey, eventKey);

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}

	@Path("{key}/action/uncancel")
	@PUT
	public Response uncancel(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey) {
		var result = uncancelHandler.uncancel(calendarKey, eventKey);

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}

	@Path("{key}/action/description")
	@PUT
	public Response description(@PathParam("calendar") String calendarKey, @PathParam("key") String eventKey, String description) {
		var result = setDescriptionHandler.setDescription(calendarKey, eventKey, description);

		if( result.isOk() ) {
			return Response.noContent().build();
		}

		return Utils.toResponse(result);
	}
}
