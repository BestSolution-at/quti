package at.bestsolution.quti.handler.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.Utils.ResultType;
import at.bestsolution.quti.model.modification.EventModificationMovedEntity;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MoveHandlerTest extends EventHandlerTest<MoveHandler> {

	public MoveHandlerTest(MoveHandler handler) {
		super(handler);
	}

	@Test
	public void moveSingle() {
		var result = handler.move(
			basicCalendarKey,
			simpleEventKey,
			null,
			ZonedDateTime.parse("2024-01-11T07:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2024-01-11T13:00:00+01:00[Europe/Vienna]")
		);
		assertTrue(result.isOk());
		var event = event(simpleEventKey);
		assertEquals(ZonedDateTime.parse("2024-01-11T07:00:00+01:00[Europe/Vienna]").withZoneSameInstant(ZoneOffset.UTC), event.start);
		assertEquals(ZonedDateTime.parse("2024-01-11T13:00:00+01:00[Europe/Vienna]").withZoneSameInstant(ZoneOffset.UTC), event.end);
	}

	@Test
	public void testMove() {
		var result = handler.move(
			basicCalendarKey,
			repeatingDailyEndlessKey,
			LocalDate.parse("2024-01-01"),
			ZonedDateTime.parse("2024-01-01T17:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2024-01-01T19:00:00+01:00[Europe/Vienna]"));
		assertTrue(result.isOk());
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationMovedEntity)
			.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
			.count());
	}

	@Test
	public void testMoveMulti() {
		var result = handler.move(
			basicCalendarKey,
			repeatingDailyEndlessKey,
			LocalDate.parse("2024-01-01"),
			ZonedDateTime.parse("2024-01-01T17:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2024-01-01T19:00:00+01:00[Europe/Vienna]"));
		assertTrue(result.isOk());

		result = handler.move(
			basicCalendarKey,
			repeatingDailyEndlessKey,
			LocalDate.parse("2024-01-01"),
			ZonedDateTime.parse("2024-01-01T18:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2024-01-01T20:00:00+01:00[Europe/Vienna]"));
		assertTrue(result.isOk());

		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationMovedEntity)
			.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
			.count());
	}

	@Test
	public void testInvalidDate() {
		var result = handler.move(
			basicCalendarKey,
			repeatingDailyEndlessKey,
			LocalDate.parse("2023-12-31"),
			ZonedDateTime.parse("2024-01-01T17:00:00+01:00[Europe/Vienna]"),
		ZonedDateTime.parse("2024-01-01T19:00:00+01:00[Europe/Vienna]"));
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}
}
