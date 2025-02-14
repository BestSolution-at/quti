package at.bestsolution.quti.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventNew;

public interface EventService {
	public Event.Data get(BuilderFactory builderFactory, String calendarKey, String eventKey, ZoneId zone);

	public String create(BuilderFactory builderFactory, String calendarKey, EventNew.Data event);

	public void delete(BuilderFactory builderFactory, String calendarKey, String eventKey);

	public void endRepeat(BuilderFactory builderFactory, String calendarKey, String eventKey,
			LocalDate endDate);

	public void move(BuilderFactory builderFactory, String calendarKey, String eventKey, ZonedDateTime start,
			ZonedDateTime end);

	public void cancel(BuilderFactory builderFactory, String calendarKey, String eventKey);

	public void uncancel(BuilderFactory builderFactory, String calendarKey, String eventKey);

	public void description(BuilderFactory builderFactory, String calendarKey, String eventKey,
			String description);

	public void update(BuilderFactory factory, String calendarKey, String eventKey, Event.Patch patch);

	public interface CancelHandler {
		public void cancel(BuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface CreateHandler {
		public String create(BuilderFactory factory, String calendarKey, EventNew.Data event);
	}

	public interface DeleteHandler {
		public void delete(BuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface EndRepeatingHandler {
		public void endRepeat(BuilderFactory factory, String calendarKey, String eventKey, LocalDate endDate);
	}

	public interface GetHandler {
		public Event.Data get(BuilderFactory factory, String calendarKey, String eventKey, ZoneId zone);
	}

	public interface MoveHandler {
		public void move(BuilderFactory factory, String calendarKey, String eventKey, ZonedDateTime start,
				ZonedDateTime end);
	}

	public interface SetDescriptionHandler {
		public void setDescription(BuilderFactory factory, String calendarKey, String eventKey,
				String description);
	}

	public interface UncancelHandler {
		public void uncancel(BuilderFactory factory, String calendarKey, String eventKey);
	}

	public interface UpdateHandler {
		public void update(BuilderFactory factory, String calendarKey, String eventKey, Event.Patch patch);
	}
}
