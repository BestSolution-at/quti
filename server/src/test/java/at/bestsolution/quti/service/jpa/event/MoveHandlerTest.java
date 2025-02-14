package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.jpa.model.modification.EventModificationMovedEntity;
import at.bestsolution.quti.service.InvalidArgumentException;
import at.bestsolution.quti.service.NotFoundException;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MoveHandlerTest extends EventHandlerTest<MoveHandlerJPA> {

	public MoveHandlerTest(MoveHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		assertThrows(InvalidArgumentException.class, () -> handler.move(
				builderFactory,
				"abcd",
				simpleEventKey.toString(),
				ZonedDateTime.parse("2024-01-11T07:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-11T13:00:00+01:00[Europe/Vienna]")));
	}

	@Test
	public void invalidEventKey() {
		assertThrows(InvalidArgumentException.class, () -> handler.move(
				builderFactory,
				basicCalendarKey.toString(),
				"abcd",
				ZonedDateTime.parse("2024-01-11T07:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-11T13:00:00+01:00[Europe/Vienna]")));
	}

	@Test
	public void invalidRepeatEventKey() {
		assertThrows(InvalidArgumentException.class, () -> handler.move(
				builderFactory,
				basicCalendarKey.toString(),
				"abcd_2024-01-11",
				ZonedDateTime.parse("2024-01-11T07:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-11T13:00:00+01:00[Europe/Vienna]")));

		assertThrows(InvalidArgumentException.class, () -> handler.move(
				builderFactory,
				basicCalendarKey.toString(),
				simpleEventKey + "_abcd",
				ZonedDateTime.parse("2024-01-11T07:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-11T13:00:00+01:00[Europe/Vienna]")));
	}

	@Test
	public void moveSingle() {
		handler.move(
				builderFactory,
				basicCalendarKey.toString(),
				simpleEventKey.toString(),
				ZonedDateTime.parse("2024-01-11T07:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-11T13:00:00+01:00[Europe/Vienna]"));
		var event = event(simpleEventKey);
		assertEquals(ZonedDateTime.parse("2024-01-11T07:00:00+01:00[Europe/Vienna]").withZoneSameInstant(ZoneOffset.UTC),
				event.start);
		assertEquals(ZonedDateTime.parse("2024-01-11T13:00:00+01:00[Europe/Vienna]").withZoneSameInstant(ZoneOffset.UTC),
				event.end);
	}

	@Test
	public void testMove() {
		handler.move(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_" + "2024-01-01",
				ZonedDateTime.parse("2024-01-01T17:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T19:00:00+01:00[Europe/Vienna]"));
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationMovedEntity)
				.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
				.count());
	}

	@Test
	public void testMoveMulti() {
		handler.move(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-01",
				ZonedDateTime.parse("2024-01-01T17:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T19:00:00+01:00[Europe/Vienna]"));

		handler.move(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-01",
				ZonedDateTime.parse("2024-01-01T18:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T20:00:00+01:00[Europe/Vienna]"));

		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationMovedEntity)
				.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
				.count());
	}

	@Test
	public void testInvalidDate() {
		assertThrows(NotFoundException.class, () -> handler.move(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey + "_2023-12-31",
				ZonedDateTime.parse("2024-01-01T17:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-01-01T19:00:00+01:00[Europe/Vienna]")));
	}
}
