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
}
