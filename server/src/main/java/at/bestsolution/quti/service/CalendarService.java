package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.service.dto.CalendarDTO;
import at.bestsolution.quti.service.dto.CalendarNewDTO;
import at.bestsolution.quti.service.dto.EventViewDTO;

public interface CalendarService {
	public Result<CalendarDTO> get(String key);
	public Result<String> create(CalendarNewDTO calendar);
	public Result<Void> update(String key, CalendarDTO.Patch patch);
	public Result<List<EventViewDTO>> eventView(String calendarKey, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultZone);

	public interface CreateHandler {
		public Result<String> create(DTOBuilderFactory factory, CalendarNewDTO calendar);
	}

	public interface GetHandler {
		public Result<CalendarDTO> get(DTOBuilderFactory factory, String key);
	}

	public interface UpdateHandler {
		public Result<Void> update(DTOBuilderFactory factory, String key, CalendarDTO.Patch patch);
	}

	public interface ViewHandler {
		public Result<List<EventViewDTO>> view(DTOBuilderFactory factory, String calendarKey, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultZone);
	}
}
