package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.service.model.Calendar;
import at.bestsolution.quti.service.model.CalendarNew;
import at.bestsolution.quti.service.model.EventView;

public interface CalendarService {
	public Result<Calendar.Data> get(DataBuilderFactory factory, String key);

	public Result<String> create(DataBuilderFactory factory, CalendarNew.Data calendar);

	public Result<Void> update(DataBuilderFactory factory, String key, Calendar.Patch patch);

	public Result<List<EventView.Data>> eventView(DataBuilderFactory factory, String calendarKey, LocalDate start,
			LocalDate end, ZoneId timezone, ZoneId resultZone);

	public interface CreateHandler {
		public Result<String> create(DataBuilderFactory factory, CalendarNew.Data calendar);
	}

	public interface GetHandler {
		public Result<Calendar.Data> get(DataBuilderFactory factory, String key);
	}

	public interface UpdateHandler {
		public Result<Void> update(DataBuilderFactory factory, String key, Calendar.Patch patch);
	}

	public interface ViewHandler {
		public Result<List<EventView.Data>> view(DataBuilderFactory factory, String calendarKey, LocalDate start,
				LocalDate end, ZoneId timezone, ZoneId resultZone);
	}
}
