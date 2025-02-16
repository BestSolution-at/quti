package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.service.model.Calendar;
import at.bestsolution.quti.service.model.CalendarNew;
import at.bestsolution.quti.service.model.EventView;

public interface CalendarService {
	public Calendar.Data get(BuilderFactory factory, String key);

	public String create(BuilderFactory factory, CalendarNew.Data calendar);

	public void update(BuilderFactory factory, String key, Calendar.Patch patch);

	public List<EventView.Data> eventView(BuilderFactory factory, String calendarKey, LocalDate start,
			LocalDate end, ZoneId timezone, ZoneId resultZone);

}
