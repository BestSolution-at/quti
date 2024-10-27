package at.bestsolution.quti.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.rest.RestDTOBuilderFactory;
import at.bestsolution.quti.rest.dto.CalendarDTO;
import at.bestsolution.quti.rest.dto.CalendarNewDTO;
import at.bestsolution.quti.rest.dto.EventViewDTO;
import at.bestsolution.quti.service.CalendarService;
import at.bestsolution.quti.service.Result;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CalendarServiceImpl implements CalendarService {
	private final RestDTOBuilderFactory builderFactory;

	private final GetHandler getHandler;
	private final CreateHandler createHandler;
	private final UpdateHandler updateHandler;
	private final ViewHandler viewHandler;

	@Inject
	public CalendarServiceImpl(
			RestDTOBuilderFactory builderFactory,
			GetHandler getHandler,
			CreateHandler createHandler,
			UpdateHandler updateHandler,
			ViewHandler viewHandler) {
		this.builderFactory = builderFactory;

		this.getHandler = getHandler;
		this.createHandler = createHandler;
		this.updateHandler = updateHandler;
		this.viewHandler = viewHandler;
	}

	@Override
	public Result<CalendarDTO> get(String key) {
		return getHandler.get(builderFactory, key);
	}

	@Override
	public Result<String> create(CalendarNewDTO calendar) {
		return createHandler.create(builderFactory, calendar);
	}

	@Override
	public Result<Void> update(String key, String patch) {
		return updateHandler.update(builderFactory, key, patch);
	}

	@Override
	public Result<List<EventViewDTO>> view(String calendarKey, LocalDate start, LocalDate end, ZoneId timezone,
			ZoneId resultZone) {
		return viewHandler.view(builderFactory, calendarKey, start, end, timezone, resultZone);
	}
}
