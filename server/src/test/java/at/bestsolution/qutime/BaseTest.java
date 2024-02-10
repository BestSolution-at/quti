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
import at.bestsolution.qutime.model.EventRepeatDailyEntity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

public class BaseTest {
    @Inject
    EntityManager em;

    public static String basicCalendarKey;
    public static String ownerlessCalendarKey;
    public static String referenceCalendarKey;

    public static String simpleEventKey;
    public static String simpleSummerEventKey;
    public static String repeatingDailyEndlessKey;

    private static EventEntity simpleEvent;
    private static EventEntity repeatingDailyEndless;

    private static boolean initDone;

    @BeforeEach
    @Transactional
    public void setupDatabase() {
        if( ! initDone ) {
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

        referenceCalendarKey = calendar.key.toString();
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

        basicCalendarKey = calendar.key.toString();
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

        simpleEventKey = event.key.toString();
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

        simpleSummerEventKey = event.key.toString();
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
        repeatPattern.event = event;
        repeatPattern.startDate = ZonedDateTime.of(LocalDate.parse("2024-01-01"), LocalTime.MIN, ZoneId.of("Europe/Vienna"));
        repeatPattern.interval = 1;
        repeatPattern.recurrenceTimezone = ZoneId.of("Europe/Vienna");

        event.repeatPattern = repeatPattern;

        em.persist(repeatPattern);
        em.persist(event);

        repeatingDailyEndlessKey = event.key.toString();
        repeatingDailyEndless = event;
    }

    private void createDatabaseCalenderNoOwner() {
        var calendar = new CalendarEntity();
        calendar.key = UUID.randomUUID();
        calendar.name = "My Calendar";
        
        em.persist(calendar);

        ownerlessCalendarKey = calendar.key.toString();
    }
}
