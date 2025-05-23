package at.bestsolution.quti.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;

import at.bestsolution.quti.calendar.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.calendar.service.jpa.model.EventEntity;
import at.bestsolution.quti.calendar.service.jpa.model.EventReferenceEntity;
import at.bestsolution.quti.calendar.service.jpa.model.modification.EventModificationCanceledEntity;
import at.bestsolution.quti.calendar.service.jpa.model.modification.EventModificationMovedEntity;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatDailyEntity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

public class BaseTest {
	@Inject
	EntityManager em;

	public static UUID basicCalendarKey;
	public static UUID ownerlessCalendarKey;
	public static UUID referenceCalendarKey;
	public static UUID writeableReferenceCalendarKey;
	public static UUID fulldayCalendarKey;

	public static UUID handler_ownerlessCalendarKey;

	public static UUID simpleEventKey;
	public static UUID simpleEventCanceledKey;
	public static UUID simpleSummerEventKey;
	public static UUID repeatingDailyEndlessKey;

	public static UUID simpleFulldayEventKey;

	private static EventEntity simpleEvent;
	private static EventEntity repeatingDailyEndless;

	@BeforeEach
	@Transactional
	public void setupDatabase() {
		creatBasicCalendar();
		em.flush();
		createDatabaseCalenderNoOwner();
		em.flush();
		createReferenceCalendar();
		em.flush();
		writableReferenceCalendar();
		em.flush();
		fulldayCalendar();
		em.flush();
	}

	private void fulldayCalendar() {
		var calendar = new CalendarEntity();
		calendar.key = UUID.randomUUID();
		calendar.name = "My fullday Calendar";
		calendar.owner = "someone@bestsolution.at";

		fulldayCalendarKey = calendar.key;
		em.persist(calendar);
		em.flush();

		createSimpleFulldayEvent(calendar);
	}

	private void createSimpleFulldayEvent(CalendarEntity calendar) {
		var event = new EventEntity();
		event.calendar = calendar;
		event.key = UUID.randomUUID();
		event.title = "Simple Event";
		event.description = "A simple none repeating event";
		event.fullday = true;
		event.start = ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]");
		event.end = ZonedDateTime.parse("2024-01-20T23:59:59+01:00[Europe/Vienna]");
		em.persist(event);

		simpleFulldayEventKey = event.key;

	}

	private void writableReferenceCalendar() {
		var calendar = new CalendarEntity();
		calendar.key = UUID.randomUUID();
		calendar.name = "My Calendar";
		calendar.owner = "someone@bestsolution.at";

		writeableReferenceCalendarKey = calendar.key;
		em.persist(calendar);
		em.flush();
	}

	private void createReferenceCalendar() {
		var calendar = new CalendarEntity();
		calendar.key = UUID.randomUUID();
		calendar.name = "My Calendar";
		calendar.owner = "somebody@bestsolution.at";

		em.persist(calendar);
		em.flush();

		{
			var reference = new EventReferenceEntity();
			reference.calendar = calendar;
			reference.event = simpleEvent;
			em.persist(reference);
		}

		{
			var reference = new EventReferenceEntity();
			reference.calendar = calendar;
			reference.event = repeatingDailyEndless;
			em.persist(reference);
		}

		referenceCalendarKey = calendar.key;
	}

	private void creatBasicCalendar() {
		var calendar = new CalendarEntity();
		calendar.key = UUID.randomUUID();
		calendar.name = "My Calendar";
		calendar.owner = "cutime@bestsolution.at";

		em.persist(calendar);
		em.flush();

		createSimpleEvent(calendar);
		createSimpleEventCanceledKey(calendar);
		createSimpleSummerEvent(calendar);
		createRepeatingDailyEndless(calendar);

		basicCalendarKey = calendar.key;
	}

	private void createSimpleEvent(CalendarEntity calendar) {
		var event = new EventEntity();
		event.calendar = calendar;
		event.key = UUID.randomUUID();
		event.title = "Simple Event";
		event.description = "A simple none repeating event";
		event.start = ZonedDateTime.parse("2024-01-10T07:00:00+01:00[Europe/Vienna]");
		event.end = ZonedDateTime.parse("2024-01-10T13:00:00+01:00[Europe/Vienna]");
		event.tags = List.of("sample-tag-1");
		em.persist(event);

		simpleEventKey = event.key;
		simpleEvent = event;
	}

	private void createSimpleEventCanceledKey(CalendarEntity calendar) {
		var event = new EventEntity();
		event.calendar = calendar;
		event.key = UUID.randomUUID();
		event.title = "Simple Event";
		event.description = "A simple none repeating event";
		event.start = ZonedDateTime.parse("2020-01-10T07:00:00+01:00[Europe/Vienna]");
		event.end = ZonedDateTime.parse("2020-01-10T13:00:00+01:00[Europe/Vienna]");
		em.persist(event);

		var mod = new EventModificationCanceledEntity();
		mod.date = LocalDate.EPOCH;
		mod.event = event;

		em.persist(mod);

		simpleEventCanceledKey = event.key;
	}

	private void createSimpleSummerEvent(CalendarEntity calendar) {
		var event = new EventEntity();
		event.calendar = calendar;
		event.key = UUID.randomUUID();
		event.title = "Simple Summer Event";
		event.description = "A simple none repeating event in summer";
		event.start = ZonedDateTime.parse("2024-06-10T07:00:00+02:00[Europe/Vienna]");
		event.end = ZonedDateTime.parse("2024-06-10T13:00:00+02:00[Europe/Vienna]");
		em.persist(event);

		simpleSummerEventKey = event.key;
	}

	private void createRepeatingDailyEndless(CalendarEntity calendar) {
		var event = new EventEntity();
		event.calendar = calendar;
		event.key = UUID.randomUUID();
		event.title = "Daily endless repeating Event";
		event.description = "A daily event repeating endless";
		event.start = ZonedDateTime.parse("2024-01-01T13:00:00+01:00[Europe/Vienna]");
		event.end = ZonedDateTime.parse("2024-01-01T16:00:00+01:00[Europe/Vienna]");

		var repeatPattern = new EventRepeatDailyEntity();
		repeatPattern.startDate = ZonedDateTime.of(LocalDate.parse("2024-01-01"), LocalTime.MIN,
				ZoneId.of("Europe/Vienna"));
		repeatPattern.interval = 1;
		repeatPattern.recurrenceTimezone = ZoneId.of("Europe/Vienna");

		event.repeatPattern = repeatPattern;

		em.persist(repeatPattern);
		em.persist(event);

		em.flush();

		{
			var move = new EventModificationMovedEntity();
			move.event = event;
			move.date = LocalDate.parse("2024-05-05");
			move.start = ZonedDateTime.parse("2024-05-05T10:00:00+02:00[Europe/Vienna]");
			move.end = ZonedDateTime.parse("2024-05-05T12:00:00+02:00[Europe/Vienna]");
			em.persist(move);
		}

		{
			var move = new EventModificationMovedEntity();
			move.event = event;
			move.date = LocalDate.parse("2024-05-08");
			move.start = ZonedDateTime.parse("2024-05-10T10:00:00+02:00[Europe/Vienna]");
			move.end = ZonedDateTime.parse("2024-05-10T12:00:00+02:00[Europe/Vienna]");
			em.persist(move);
		}

		{
			var cancel = new EventModificationCanceledEntity();
			cancel.event = event;
			cancel.date = LocalDate.parse("2024-05-09");
			em.persist(cancel);
		}

		repeatingDailyEndlessKey = event.key;
		repeatingDailyEndless = event;
	}

	private void createDatabaseCalenderNoOwner() {
		{
			var calendar = new CalendarEntity();
			calendar.key = UUID.randomUUID();
			calendar.name = "My Calendar";

			em.persist(calendar);

			ownerlessCalendarKey = calendar.key;
		}
		{
			var calendar = new CalendarEntity();
			calendar.key = UUID.randomUUID();
			calendar.name = "My Handler Calendar";

			em.persist(calendar);

			handler_ownerlessCalendarKey = calendar.key;
		}
	}
}
