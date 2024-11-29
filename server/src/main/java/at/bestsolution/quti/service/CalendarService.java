package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.rest.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.rest.dto.EventViewDTOImpl;
import at.bestsolution.quti.service.dto.CalendarDTO;

public interface CalendarService {
	public Result<CalendarDTO> get(String key);
	public Result<String> create(CalendarNewDTOImpl calendar);
	public Result<Void> update(String key, String patch);
	public Result<List<EventViewDTOImpl>> view(String calendarKey, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultZone);

	public interface CreateHandler {
		public Result<String> create(DTOBuilderFactory factory, CalendarNewDTOImpl calendar);
	}

	public interface GetHandler {
		public Result<CalendarDTO> get(DTOBuilderFactory factory, String key);
	}

	public interface UpdateHandler {
		public Result<Void> update(DTOBuilderFactory factory, String key, String patch);
	}

	public interface ViewHandler {
		public Result<List<EventViewDTOImpl>> view(DTOBuilderFactory factory, String calendarKey, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultZone);
	}
}
