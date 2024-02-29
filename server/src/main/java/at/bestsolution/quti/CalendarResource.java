package at.bestsolution.quti;

import java.net.URI;

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
		var parsedKey = Utils.parseUUID(key, "key");
		if (parsedKey.response() != null) {
			return parsedKey.response();
		}

		var calendar = getHandler.get(parsedKey.value());
		if (calendar == null) {
			return Utils.notFound(String.format("Could not find calendar with '%s'", key));
		}
		return Response.ok(calendar).build();
	}

	@POST
	public Response create(CalendarNewDTO calendar) {
		var key = createHandler.create(calendar);
		return Response.created(URI.create("/api/calendar/" + key)).build();
	}

	@PATCH
	@Path("{key}")
	public Response update(@PathParam("key") String key, String patch) {
		var parsedKey = Utils.parseUUID(key, "key");
		var parsedPatch = Utils.parseJsonPatch(patch, "patch");

		if (parsedKey.response() != null) {
			return parsedKey.response();
		}
		if (parsedPatch.response() != null) {
			return parsedPatch.response();
		}

		var result = updateHandler.update(parsedKey.value(), parsedPatch.value());
		if (result.isOk()) {
			return Response.ok().build();
		}
		return Utils.toResponse(result);
	}

	@GET
	@Path("{key}/view")
	public Response views(
			@PathParam("key") String key,
			@QueryParam("from") String from,
			@QueryParam("to") String to,
			@QueryParam("timezone") String timezone,
			@HeaderParam("timezone") String resultTimeZone) {

		var parsedCalendarKey = Utils.parseUUID(key, "request path");
		var parsedFrom = Utils.parseLocalDate(from, "query parameter 'from'");
		var parsedTo = Utils.parseLocalDate(to, "query parameter 'to'");
		var parsedZone = Utils.parseZone(timezone, "query parameter 'timezone'");
		var parsedResultTimeZone = resultTimeZone == null ? parsedZone
				: Utils.parseZone(resultTimeZone, "header parameter 'timezone'");

		if (parsedCalendarKey.response() != null) {
			return parsedCalendarKey.response();
		}
		if (parsedFrom.response() != null) {
			return parsedFrom.response();
		}
		if (parsedTo.response() != null) {
			return parsedTo.response();
		}
		if (parsedZone.response() != null) {
			return parsedZone.response();
		}
		if (parsedResultTimeZone.response() != null) {
			return parsedResultTimeZone.response();
		}

		return Response.ok(
				viewHandler.view(
						parsedCalendarKey.value(),
						parsedFrom.value(),
						parsedTo.value(),
						parsedZone.value(),
						parsedResultTimeZone.value()))
				.build();
	}
}
