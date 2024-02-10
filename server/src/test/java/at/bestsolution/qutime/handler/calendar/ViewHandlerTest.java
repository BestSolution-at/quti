package at.bestsolution.qutime.handler.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void testViewDaily() {
        var result = handler.view(
            UUID.fromString(basicCalendarKey), 
            LocalDate.parse("2024-01-01"), 
            LocalDate.parse("2024-01-31"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        // 31 Repeating + 1 Simple
        assertEquals(32, result.size());
        assertEquals(LocalDate.parse("2024-01-01"), result.get(0).start().toLocalDate());
        assertEquals(LocalDate.parse("2024-01-31"), result.get(31).start().toLocalDate());
    }

    @Test
    public void testViewDailyRef() {
        var result = handler.view(
            UUID.fromString(referenceCalendarKey), 
            LocalDate.parse("2024-01-01"), 
            LocalDate.parse("2024-01-31"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        // 31 Repeating + 1 Simple
        assertEquals(32, result.size());
        assertEquals(LocalDate.parse("2024-01-01"), result.get(0).start().toLocalDate());
        assertEquals(LocalDate.parse("2024-01-31"), result.get(31).start().toLocalDate());
    }

    @Test
    void testViewDailyAboveDaylightSaving() {
        var result = handler.view(
            UUID.fromString(basicCalendarKey), 
            LocalDate.parse("2024-03-25"), 
            LocalDate.parse("2024-04-05"), 
            ZoneId.of("Europe/Vienna"), 
            ZoneId.of("Europe/Vienna"));
        // 6 in March + 5 April
        assertEquals(12, result.size());
    }
}
