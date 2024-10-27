package at.bestsolution.quti.service.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.jpa.RepeatUtils;

public class RepeatUtilsTest {
@Test
	public void testFromRepeatWeeklySimple() {
		var results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 1,
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(5, results.size());
		assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
		results.forEach(r -> {
			assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
		});

		results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 1,
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-29T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(5, results.size());
		assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
		results.forEach(r -> {
			assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
		});

		results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 1,
				ZonedDateTime.parse("2023-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2023-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2023-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(5, results.size());
		assertEquals(LocalDate.parse("2023-01-02"), results.get(0));
		results.forEach(r -> {
			assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
		});
	}

	@Test
	public void testFromRepeatWeeklyMulti() {
		var results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), 1,
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(10, results.size());
		assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
		assertEquals(LocalDate.parse("2024-01-02"), results.get(1));

		results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), 1,
		ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-29T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(9, results.size());
	}

	@Test
	public void testFromRepeatBiweekly() {
		var results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 2,
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();

		assertEquals(3, results.size());
		assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
		results.forEach(r -> {
			assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
		});

		results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 2,
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-02T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-16T23:59:00+01:00[Europe/Vienna]")).toList();

		assertEquals(1, results.size());
		assertEquals(LocalDate.parse("2024-01-15"), results.get(0));
	}

	@Test
	public void testFromRepeatBiweeklyMulti() {
		var results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), 2,
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
		System.err.println(results);
		assertEquals(6, results.size());
		assertEquals(LocalDate.parse("2024-01-01"), results.get(0));
		assertEquals(LocalDate.parse("2024-01-02"), results.get(1));
	}

	@Test
	public void testFromRepeatWeeklyEmpty() {
		var results = RepeatUtils.fromRepeatWeekly(List.of(DayOfWeek.MONDAY), 1,
				ZonedDateTime.parse("2024-01-02T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-02T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-06T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(0, results.size());
	}

	@Test
	public void testFromRepeatDailySimple() {
		var results = RepeatUtils.fromRepeatDaily(3,
				ZonedDateTime.parse("2023-12-02T08:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-31T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(11, results.size());
	}

	@Test
	public void testFromRepeatDailyEmpty() {
		var results = RepeatUtils.fromRepeatDaily(3,
				ZonedDateTime.parse("2023-12-02T08:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-02T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-03T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(0, results.size());

		results = RepeatUtils.fromRepeatDaily(3,
				ZonedDateTime.parse("2024-01-02T08:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T00:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T23:59:00+01:00[Europe/Vienna]")).toList();
		assertEquals(0, results.size());
	}

}
