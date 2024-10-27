package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.dto.CalendarDTO;
import at.bestsolution.quti.dto.CalendarNewDTO;
import at.bestsolution.quti.dto.EventViewDTO;

public interface CalendarService {
	public Result<CalendarDTO> get(String key);
	public Result<String> create(CalendarNewDTO calendar);
	public Result<Void> update(String key, String patch);
	public Result<List<EventViewDTO>> view(String calendarKey, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultZone);

	public interface CreateHandler {
		public Result<String> create(CalendarNewDTO calendar);
	}

	public interface GetHandler {
		public Result<CalendarDTO> get(String key);
	}

	public interface UpdateHandler {
		public Result<Void> update(String key, String patch);
	}

	public interface ViewHandler {
		public Result<List<EventViewDTO>> view(String calendarKey, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultZone);
	}
}
