package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventNew;

public interface EventService {
	public Result<Event.Data> get(BuilderFactory builderFactory, String calendarKey, String eventKey, ZoneId zone);

	public Result<String> create(BuilderFactory builderFactory, String calendarKey, EventNew.Data event);

	public Result<Void> delete(BuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> endRepeat(BuilderFactory builderFactory, String calendarKey, String eventKey,
			LocalDate endDate);

	public Result<Void> move(BuilderFactory builderFactory, String calendarKey, String eventKey, ZonedDateTime start,
			ZonedDateTime end);

	public Result<Void> cancel(BuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> uncancel(BuilderFactory builderFactory, String calendarKey, String eventKey);

	public Result<Void> description(BuilderFactory builderFactory, String calendarKey, String eventKey,
			String description);

	public interface CancelHandler {
		public Result<Void> cancel(BuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface CreateHandler {
		public Result<String> create(BuilderFactory factory, String calendarKey, EventNew.Data event);
	}

	public interface DeleteHandler {
		public Result<Void> delete(BuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface EndRepeatingHandler {
		public Result<Void> endRepeat(BuilderFactory factory, String calendarKey, String eventKey, LocalDate endDate);
	}

	public interface GetHandler {
		public Result<Event.Data> get(BuilderFactory factory, String calendarKey, String eventKey, ZoneId zone);
	}

	public interface MoveHandler {
		public Result<Void> move(BuilderFactory factory, String calendarKey, String eventKey, ZonedDateTime start,
				ZonedDateTime end);
	}

	public interface SetDescriptionHandler {
		public Result<Void> setDescription(BuilderFactory factory, String calendarKey, String eventKey,
				String description);
	}

	public interface UncancelHandler {
		public Result<Void> uncancel(BuilderFactory factory, String calendarKey, String eventKey);
	}

}
