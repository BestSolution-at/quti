package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.service.dto.EventDTO;
import at.bestsolution.quti.service.dto.EventNewDTO;

public interface EventService {
	public Result<EventDTO> get(DTOBuilderFactory builderFactory, String calendarKey, String eventKey, ZoneId zone);

	public Result<String> create(DTOBuilderFactory builderFactory, String calendarKey, EventNewDTO event);

	public Result<Void> delete(DTOBuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> endRepeat(DTOBuilderFactory builderFactory, String calendarKey, String eventKey,
			LocalDate endDate);

	public Result<Void> move(DTOBuilderFactory builderFactory, String calendarKey, String eventKey, ZonedDateTime start,
			ZonedDateTime end);

	public Result<Void> cancel(DTOBuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> uncancel(DTOBuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> description(DTOBuilderFactory builderFactory, String calendarKey, String eventKey,
			String description);

	public interface CancelHandler {
		public Result<Void> cancel(DTOBuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface CreateHandler {
		public Result<String> create(DTOBuilderFactory factory, String calendarKey, EventNewDTO event);
	}

	public interface DeleteHandler {
		public Result<Void> delete(DTOBuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface EndRepeatingHandler {
		public Result<Void> endRepeat(DTOBuilderFactory factory, String calendarKey, String eventKey, LocalDate endDate);
	}

	public interface GetHandler {
		public Result<EventDTO> get(DTOBuilderFactory factory, String calendarKey, String eventKey, ZoneId zone);
	}

	public interface MoveHandler {
		public Result<Void> move(DTOBuilderFactory factory, String calendarKey, String eventKey, ZonedDateTime start,
				ZonedDateTime end);
	}

	public interface SetDescriptionHandler {
		public Result<Void> setDescription(DTOBuilderFactory factory, String calendarKey, String eventKey,
				String description);
	}

	public interface UncancelHandler {
		public Result<Void> uncancel(DTOBuilderFactory factory, String calendarKey, String eventKey);
	}

}
