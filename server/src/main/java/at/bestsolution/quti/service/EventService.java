package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.dto.EventDTO;
import at.bestsolution.quti.dto.EventNewDTO;

public interface EventService {
	public Result<EventDTO> get(String calendarKey, String eventKey, ZoneId zone);
	public Result<String> create(String calendarKey, EventNewDTO event);
	public Result<Void> delete(String calendarKey, String eventKey);
	public Result<Void> endRepeat(String calendarKey, String eventKey, LocalDate endDate);
	public Result<Void> move(String calendarKey, String eventKey, ZonedDateTime start, ZonedDateTime end);
	public Result<Void> cancel(String calendarKey, String eventKey);
	public Result<Void> uncancel(String calendarKey, String eventKey);
	public Result<Void> setDescription(String calendarKey, String eventKey, String description);

	public interface CancelHandler {
		public Result<Void> cancel(String calendarKey, String eventKey);
	}

	public interface CreateHandler {
		public Result<String> create(String calendarKey, EventNewDTO event);
	}

	public interface DeleteHandler {
		public Result<Void> delete(String calendarKey, String eventKey);
	}

	public interface EndRepeatingHandler {
		public Result<Void> endRepeat(String calendarKey, String eventKey, LocalDate endDate);
	}

	public interface GetHandler {
		public Result<EventDTO> get(String calendarKey, String eventKey, ZoneId zone);
	}

	public interface MoveHandler {
		public Result<Void> move(String calendarKey, String eventKey, ZonedDateTime start, ZonedDateTime end);
	}

	public interface SetDescriptionHandler {
		public Result<Void> setDescription(String calendarKey, String eventKey, String description);
	}

	public interface UncancelHandler {
		public Result<Void> uncancel(String calendarKey, String eventKey);
	}

}