package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventNew;

public interface EventService {
	public Result<Event.Data> get(DataBuilderFactory builderFactory, String calendarKey, String eventKey, ZoneId zone);

	public Result<String> create(DataBuilderFactory builderFactory, String calendarKey, EventNew.Data event);

	public Result<Void> delete(DataBuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> endRepeat(DataBuilderFactory builderFactory, String calendarKey, String eventKey,
			LocalDate endDate);

	public Result<Void> move(DataBuilderFactory builderFactory, String calendarKey, String eventKey, ZonedDateTime start,
			ZonedDateTime end);

	public Result<Void> cancel(DataBuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> uncancel(DataBuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> description(DataBuilderFactory builderFactory, String calendarKey, String eventKey,
			String description);

	public interface CancelHandler {
		public Result<Void> cancel(DataBuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface CreateHandler {
		public Result<String> create(DataBuilderFactory factory, String calendarKey, EventNew.Data event);
	}

	public interface DeleteHandler {
		public Result<Void> delete(DataBuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface EndRepeatingHandler {
		public Result<Void> endRepeat(DataBuilderFactory factory, String calendarKey, String eventKey, LocalDate endDate);
	}

	public interface GetHandler {
		public Result<Event.Data> get(DataBuilderFactory factory, String calendarKey, String eventKey, ZoneId zone);
	}

	public interface MoveHandler {
		public Result<Void> move(DataBuilderFactory factory, String calendarKey, String eventKey, ZonedDateTime start,
				ZonedDateTime end);
	}

	public interface SetDescriptionHandler {
		public Result<Void> setDescription(DataBuilderFactory factory, String calendarKey, String eventKey,
				String description);
	}

	public interface UncancelHandler {
		public Result<Void> uncancel(DataBuilderFactory factory, String calendarKey, String eventKey);
	}

}
