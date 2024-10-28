package at.bestsolution.quti.rest;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.rest.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.service.CalendarService;
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
	private final CalendarService service;

	@Inject
	public CalendarResource(CalendarService service) {
		this.service = service;
	}

	@GET
	@Path("{key}")
	public Response get(@PathParam("key") String key) {
		var result = service.get(key);

		if (result.isOk()) {
			return Response.ok(result.value()).build();
		}
		return Utils.toResponse(result);
	}

	@POST
	public Response create(CalendarNewDTOImpl calendar) {
		var result = service.create(calendar);
		if( result.isOk() ) {
			return Response.created(URI.create("/api/calendar/" + result.value())).entity(result.value()).build();
		}
		return Utils.toResponse(result);
	}

	@PATCH
	@Path("{key}")
	public Response update(@PathParam("key") String key, String patch) {
		var result = service.update(key, patch);
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

		var result = service.view(
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
