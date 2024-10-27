package at.bestsolution.quti;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.dto.CalendarNewDTO;
import at.bestsolution.quti.handler.calendar.CreateHandler;
import at.bestsolution.quti.handler.calendar.GetHandler;
import at.bestsolution.quti.handler.calendar.UpdateHandler;
import at.bestsolution.quti.handler.calendar.ViewHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/api/calendar")
@Produces(MediaType.APPLICATION_JSON)
public class CalendarResource {

	private final GetHandler getHandler;
	private final CreateHandler createHandler;
	private final UpdateHandler updateHandler;
	private final ViewHandler viewHandler;

	@Inject
	public CalendarResource(GetHandler getHandler, CreateHandler createHandler, UpdateHandler updateHandler,
			ViewHandler viewHandler) {
		this.getHandler = getHandler;
		this.createHandler = createHandler;
		this.updateHandler = updateHandler;
		this.viewHandler = viewHandler;
	}

	@GET
	@Path("{key}")
	public Response get(@PathParam("key") String key) {
		var result = getHandler.get(key);

		if (result.isOk()) {
			return Response.ok(result.value()).build();
		}
		return Utils.toResponse(result);
	}

	@POST
	public Response create(CalendarNewDTO calendar) {
		var result = createHandler.create(calendar);
		if( result.isOk() ) {
			return Response.created(URI.create("/api/calendar/" + result.value())).entity(result.value()).build();
		}
		return Utils.toResponse(result);
	}

	@PATCH
	@Path("{key}")
	public Response update(@PathParam("key") String key, String patch) {
		var result = updateHandler.update(key, patch);
		if (result.isOk()) {
			return Response.ok().build();
		}
		return Utils.toResponse(result);
	}

	@GET
	@Path("{key}/view")
	public Response views(
			@PathParam("key") String key,
			@QueryParam("from") LocalDate from,
			@QueryParam("to") LocalDate to,
			@QueryParam("timezone") ZoneId timezone,
			@HeaderParam("timezone") ZoneId resultTimeZone) {

		var result = viewHandler.view(
					key,
					from,
					to,
					timezone,
					resultTimeZone);

		if( result.isOk() ) {
			return Response.ok(result.value()).build();
		}

		return Utils.toResponse(result);
	}
}
