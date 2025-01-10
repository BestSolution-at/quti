package at.bestsolution.quti.rest;

import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.rest.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.rest.dto.CalendarPatchDTOImpl;
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
	private final RestDTOBuilderFactory builderFactory;
	private final CalendarService service;
	private final CalendarResourceResponseBuilder responseBuilder;

	@Inject
	public CalendarResource(
			CalendarService service,
			CalendarResourceResponseBuilder responseBuilder,
			RestDTOBuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
		this.service = service;
		this.responseBuilder = responseBuilder;
	}

	@GET
	@Path("{key}")
	public Response get(@PathParam("key") String key) {
		var result = service.get(builderFactory, key);
		if (result.isOk()) {
			return responseBuilder.get(result.value(), key).build();
		}
		return RestUtils.toResponse(result);
	}

	@POST
	public Response create(CalendarNewDTOImpl calendar) {
		var result = service.create(builderFactory, calendar);
		if (result.isOk()) {
			return responseBuilder.create(result.value(), calendar).build();
		}
		return RestUtils.toResponse(result);
	}

	@PATCH
	@Path("{key}")
	public Response update(@PathParam("key") String key, CalendarPatchDTOImpl patch) {
		var result = service.update(builderFactory, key, patch);
		if (result.isOk()) {
			return responseBuilder.update(key, patch).build();
		}
		return RestUtils.toResponse(result);
	}

	@GET
	@Path("{key}/view")
	public Response eventView(
			@PathParam("key") String key,
			@QueryParam("from") LocalDate start,
			@QueryParam("to") LocalDate end,
			@QueryParam("timezone") ZoneId timezone,
			@HeaderParam("timezone") ZoneId resultTimeZone) {
		var result = service.eventView(builderFactory, key, start, end, timezone, resultTimeZone);
		if (result.isOk()) {
			return responseBuilder.eventView(result.value(), key, start, end, timezone, resultTimeZone).build();
		}
		return RestUtils.toResponse(result);
	}
}
