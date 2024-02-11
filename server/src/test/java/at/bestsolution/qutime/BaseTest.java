package at.bestsolution.qutime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;

import at.bestsolution.qutime.model.CalendarEntity;
import at.bestsolution.qutime.model.EventEntity;
import at.bestsolution.qutime.model.EventReferenceEntity;
import at.bestsolution.qutime.model.modification.EventModificationMovedEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatDailyEntity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

public class BaseTest {
	@Inject
	EntityManager em;

	public static UUID basicCalendarKey;
	public static UUID ownerlessCalendarKey;
	public static UUID referenceCalendarKey;

	public static UUID simpleEventKey;
	public static UUID simpleSummerEventKey;
	public static UUID repeatingDailyEndlessKey;

	private static EventEntity simpleEvent;
	private static EventEntity repeatingDailyEndless;

	private static boolean initDone;

	@BeforeEach
	@Transactional
	public void setupDatabase() {
		if (!initDone) {
			initDone = true;
			creatBasicCalendar();
			em.flush();
			createDatabaseCalenderNoOwner();
			em.flush();
			createReferenceCalendar();
			em.flush();
		}
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
		createSimpleSummerEvent(calendar);
		createRepeatingDailyEndless(calendar);

		basicCalendarKey = calendar.key;
	}

	private void createSimpleEvent(CalendarEntity calendar) {
		var event = new EventEntity();
		event.calendar = calendar;
		event.key = UUID.randomUUID();
		event.title = "Simple Event";
		event.desription = "A simple none repeating event";
		event.start = ZonedDateTime.parse("2024-01-10T07:00:00+01:00[Europe/Vienna]");
		event.end = ZonedDateTime.parse("2024-01-10T13:00:00+01:00[Europe/Vienna]");
		em.persist(event);

		simpleEventKey = event.key;
		simpleEvent = event;
	}

	private void createSimpleSummerEvent(CalendarEntity calendar) {
		var event = new EventEntity();
		event.calendar = calendar;
		event.key = UUID.randomUUID();
		event.title = "Simple Summer Event";
		event.desription = "A simple none repeating event in summer";
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
		event.desription = "A daily event repeating endless";
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

		repeatingDailyEndlessKey = event.key;
		repeatingDailyEndless = event;
	}

	private void createDatabaseCalenderNoOwner() {
		var calendar = new CalendarEntity();
		calendar.key = UUID.randomUUID();
		calendar.name = "My Calendar";

		em.persist(calendar);

		ownerlessCalendarKey = calendar.key;
	}
}
