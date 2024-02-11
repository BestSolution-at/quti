package at.bestsolution.qutime.handler.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.qutime.BaseTest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ViewHandlerTest extends BaseTest {
    @Inject
    ViewHandler handler;

    @Test
    public void testFromRepeatWeeklySimple() {
        var results = ViewHandler.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 1, 
            ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
        assertEquals(5, results.size());
        assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
        results.forEach( r -> {
            assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
        });

        results = ViewHandler.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 1, 
            ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-29T23:59:00+01:00[Europe/Vienna]")).toList();
        assertEquals(5, results.size());
        assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
        results.forEach( r -> {
            assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
        });

        results = ViewHandler.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 1, 
            ZonedDateTime.parse("2023-01-01T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2023-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
        assertEquals(5, results.size());
        assertEquals(LocalDate.parse("2023-01-02"), results.get(0));
        results.forEach( r -> {
            assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
        });
    }

    @Test
    public void testFromRepeatWeeklyMulti() {
        var results = ViewHandler.fromRepeatWeekly(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), 1, 
            ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
        assertEquals(10, results.size());
        assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
        assertEquals(LocalDate.parse("2024-01-02"), results.get(1));

        results = ViewHandler.fromRepeatWeekly(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), 1, 
            ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-29T23:59:00+01:00[Europe/Vienna]")).toList();
        assertEquals(9, results.size());
    }

    @Test
    public void testFromRepeatBiweekly() {
        var results = ViewHandler.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 2, 
            ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
        System.err.println(results);
        assertEquals(3, results.size());
        assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
        results.forEach( r -> {
            assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
        });
    }

    @Test
    public void testFromRepeatBiweeklyMulti() {
        var results = ViewHandler.fromRepeatWeekly(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), 2, 
            ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
        System.err.println(results);
        assertEquals(6, results.size());
        assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
        assertEquals(LocalDate.parse("2024-01-02"), results.get(1));
    }

    @Test
    public void testFromRepeatWeeklyEmpty() {
        var results = ViewHandler.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 1, 
            ZonedDateTime.parse("2024-01-02T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-06T23:59:00+01:00[Europe/Vienna]")).toList();
        assertEquals(0, results.size());
    }

    @Test
    public void testFromRepeatDailySimple() {
        var results = ViewHandler.fromRepeatDaily(3, 
            ZonedDateTime.parse("2023-12-02T08:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
        assertEquals(11, results.size());
    }

    @Test
    public void testFromRepeatDailyEmpty() {
        var results = ViewHandler.fromRepeatDaily(3, 
            ZonedDateTime.parse("2023-12-02T08:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-02T00:00:00+01:00[Europe/Vienna]"), 
            ZonedDateTime.parse("2024-01-03T23:59:00+01:00[Europe/Vienna]")).toList();
            assertEquals(0, results.size());
    }

    @Test
    public void testInvalidParams() {
        assertThrows(NullPointerException.class, () -> 
            handler.view(
                null, 
                null, 
                null, 
                null, 
                null)
        );
        assertThrows(NullPointerException.class, () -> 
            handler.view(
                basicCalendarKey, 
                null, 
                null, 
                null, 
                null)
        );
        assertThrows(NullPointerException.class, () -> 
            handler.view(
                basicCalendarKey, 
                LocalDate.parse("2024-01-01"), 
                null, 
                null, 
                null)
        );
        assertThrows(NullPointerException.class, () -> 
            handler.view(
                basicCalendarKey, 
                LocalDate.parse("2024-01-01"), 
                LocalDate.parse("2024-03-01"), 
                null, 
                null)
        );
        assertThrows(NullPointerException.class, () -> 
            handler.view(
                basicCalendarKey, 
                LocalDate.parse("2024-01-01"), 
                LocalDate.parse("2024-03-01"), 
                ZoneId.of("Europe/Vienna"), 
                null)
        );
    }

    @Test
    public void testViewDaily() {
        var result = handler.view(
            basicCalendarKey, 
            LocalDate.parse("2024-01-01"), 
            LocalDate.parse("2024-03-01"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        // 1 Simple
        // 31 Repeating Jan
        // 29 Repeating Feb
        // 1 Repeating March
        assertEquals(62, result.size());
        assertEquals(LocalDate.parse("2024-01-01"), result.get(0).start.toLocalDate());
        assertEquals(LocalDate.parse("2024-01-31"), result.get(31).start.toLocalDate());

        result = handler.view(
            basicCalendarKey, 
            LocalDate.parse("2025-02-01"), 
            LocalDate.parse("2025-03-01"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        assertEquals(29, result.size());
        assertEquals(LocalDate.parse("2025-02-01"), result.get(0).start.toLocalDate());
        assertEquals(LocalDate.parse("2025-03-01"), result.get(28).start.toLocalDate());
    }

    @Test
    public void testViewDailyRef() {
        var result = handler.view(
            referenceCalendarKey, 
            LocalDate.parse("2024-01-01"), 
            LocalDate.parse("2024-01-31"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        // 31 Repeating + 1 Simple
        assertEquals(32, result.size());
        assertEquals(LocalDate.parse("2024-01-01"), result.get(0).start.toLocalDate());
        assertEquals(LocalDate.parse("2024-01-31"), result.get(31).start.toLocalDate());
    }

    @Test
    void testViewDailyAboveDaylightSaving() {
        var result = handler.view(
            basicCalendarKey, 
            LocalDate.parse("2024-03-25"), 
            LocalDate.parse("2024-04-05"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        // 6 in March + 5 April
        assertEquals(12, result.size());
    }

    @Test
    public void testViewDailyCustResultTimezone() {
        var result = handler.view(
            referenceCalendarKey, 
            LocalDate.parse("2024-01-10"), 
            LocalDate.parse("2024-01-10"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Z"));
        // 31 Repeating + 1 Simple
        assertEquals(2, result.size());
        assertEquals(ZonedDateTime.parse("2024-01-10T06:00Z"), result.get(0).start);
        assertEquals(ZonedDateTime.parse("2024-01-10T12:00Z"), result.get(1).start);
    }

    @Test
    public void testDaylightSavingCustResultTimezone() {
        var result = handler.view(
            basicCalendarKey, 
            LocalDate.parse("2024-03-25"), 
            LocalDate.parse("2024-04-05"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Z"));
        assertEquals(12, result.size());
        assertEquals(ZonedDateTime.parse("2024-03-25T12:00Z"), result.get(0).start);
        assertEquals(ZonedDateTime.parse("2024-04-05T11:00Z"), result.get(11).start);
    }


    @Test
    public void testMoveEvents() {
        var result = handler.view(
            basicCalendarKey, 
            LocalDate.parse("2024-05-04"), 
            LocalDate.parse("2024-05-06"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        assertEquals(3, result.size());

        result = handler.view(
            basicCalendarKey, 
            LocalDate.parse("2024-05-07"), 
            LocalDate.parse("2024-05-09"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        assertEquals(2, result.size());
    }

    @Test
    public void testMoveRefEvents() {
        var result = handler.view(
            referenceCalendarKey, 
            LocalDate.parse("2024-05-04"), 
            LocalDate.parse("2024-05-06"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        assertEquals(3, result.size());

        result = handler.view(
            referenceCalendarKey, 
            LocalDate.parse("2024-05-07"), 
            LocalDate.parse("2024-05-09"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        assertEquals(2, result.size());
    }
}
