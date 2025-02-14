package at.bestsolution.quti.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.service.CalendarService;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.model.Calendar;
import at.bestsolution.quti.service.model.CalendarNew;
import at.bestsolution.quti.service.model.EventView;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CalendarServiceImpl implements CalendarService {

	private final GetHandler getHandler;
	private final CreateHandler createHandler;
	private final UpdateHandler updateHandler;
	private final ViewHandler viewHandler;

	@Inject
	public CalendarServiceImpl(
			GetHandler getHandler,
			CreateHandler createHandler,
			UpdateHandler updateHandler,
			ViewHandler viewHandler) {

		this.getHandler = getHandler;
		this.createHandler = createHandler;
		this.updateHandler = updateHandler;
		this.viewHandler = viewHandler;
	}

	@Override
	public Calendar.Data get(BuilderFactory builderFactory, String key) {
		return getHandler.get(builderFactory, key);
	}

	@Override
	public String create(BuilderFactory builderFactory, CalendarNew.Data calendar) {
		return createHandler.create(builderFactory, calendar);
	}

	@Override
	public void update(BuilderFactory builderFactory, String key, Calendar.Patch patch) {
		updateHandler.update(builderFactory, key, patch);
	}

	@Override
	public List<EventView.Data> eventView(
			BuilderFactory builderFactory,
			String calendarKey,
			LocalDate start,
			LocalDate end,
			ZoneId timezone,
			ZoneId resultZone) {
		return viewHandler.view(builderFactory, calendarKey, start, end, timezone, resultZone);
	}
}
