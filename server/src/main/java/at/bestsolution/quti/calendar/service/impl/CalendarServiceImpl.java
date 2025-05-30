// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import jakarta.inject.Singleton;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.CalendarService;
import at.bestsolution.quti.calendar.service.InvalidArgumentException;
import at.bestsolution.quti.calendar.service.InvalidContentException;
import at.bestsolution.quti.calendar.service.model.Calendar;
import at.bestsolution.quti.calendar.service.model.CalendarNew;
import at.bestsolution.quti.calendar.service.model.EventView;
import at.bestsolution.quti.calendar.service.NotFoundException;

@Singleton
public class CalendarServiceImpl implements CalendarService {
	private final CreateHandler createHandler;
	private final GetHandler getHandler;
	private final UpdateHandler updateHandler;
	private final EventViewHandler eventViewHandler;

	public CalendarServiceImpl(CreateHandler createHandler, GetHandler getHandler, UpdateHandler updateHandler, EventViewHandler eventViewHandler) {
		this.createHandler = createHandler;
		this.getHandler = getHandler;
		this.updateHandler = updateHandler;
		this.eventViewHandler = eventViewHandler;
	}

	@Override
	public String create(BuilderFactory _factory, CalendarNew.Data calendar)
			throws InvalidContentException {
		return createHandler.create(_factory, calendar);
	}

	@Override
	public Calendar.Data get(BuilderFactory _factory, String key)
			throws NotFoundException,
			InvalidArgumentException {
		return getHandler.get(_factory, key);
	}

	@Override
	public void update(BuilderFactory _factory, String key, Calendar.Patch changes)
			throws NotFoundException,
			InvalidArgumentException {
		updateHandler.update(_factory, key, changes);
	}

	@Override
	public List<EventView.Data> eventView(BuilderFactory _factory, String key, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultTimeZone)
			throws NotFoundException,
			InvalidArgumentException {
		return eventViewHandler.eventView(_factory, key, start, end, timezone, resultTimeZone);
	}

	public interface CreateHandler {
		public String create(BuilderFactory _factory, CalendarNew.Data calendar)
				throws InvalidContentException;
	}

	public interface GetHandler {
		public Calendar.Data get(BuilderFactory _factory, String key)
				throws NotFoundException,
				InvalidArgumentException;
	}

	public interface UpdateHandler {
		public void update(BuilderFactory _factory, String key, Calendar.Patch changes)
				throws NotFoundException,
				InvalidArgumentException;
	}

	public interface EventViewHandler {
		public List<EventView.Data> eventView(BuilderFactory _factory, String key, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultTimeZone)
				throws NotFoundException,
				InvalidArgumentException;
	}

}
