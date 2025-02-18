package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class EndRepeatingHandlerTest extends EventHandlerTest<EndRepeatingHandlerJPA> {
	private final MoveHandlerJPA moveHandler;

	@Inject
	public EndRepeatingHandlerTest(EndRepeatingHandlerJPA handler, MoveHandlerJPA moveHandler) {
		super(handler);
		this.moveHandler = moveHandler;
	}

	@Test
	public void invalidCalendarKey() {
		var result = handler.endRepeat("abcd", simpleEventKey.toString(), null);
		assertFalse(result.isOk());
	}

	@Test
	public void invalidEventKey() {
		var result = handler.endRepeat(basicCalendarKey.toString(), "abcd", null);
		assertFalse(result.isOk());
	}

	@Test
	public void endRepeating() {
		var newEnd = LocalDate.parse("2024-01-10");
		var result = handler.endRepeat(
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString(),
				newEnd);

		assertTrue(result.isOk());

		var end = event(repeatingDailyEndlessKey).repeatPattern.endDate;
		assertNotNull(end);
		assertEquals(end.toLocalDate(), newEnd);
	}

	@Test
	public void invalidEnd() {
		var result = handler.endRepeat(
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString(),
				LocalDate.parse("2023-12-31"));
		assertFalse(result.isOk());
	}

	@Test
	public void endRepeatingClearModifications() {
		moveHandler.move(
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-05-11",
				ZonedDateTime.parse("2024-05-11T18:00:00+01:00[Europe/Vienna]"),
				ZonedDateTime.parse("2024-05-11T20:00:00+01:00[Europe/Vienna]"));

		assertEquals(1,
				modifications(repeatingDailyEndlessKey).stream().filter(e -> e.date.equals(LocalDate.parse("2024-05-11")))
						.count());
		modifications(repeatingDailyEndlessKey).stream().forEach(e -> System.err.println(e.date));

		handler.endRepeat(basicCalendarKey.toString(), repeatingDailyEndlessKey.toString(),
				LocalDate.parse("2024-05-10"));
		assertEquals(0,
				modifications(repeatingDailyEndlessKey).stream().filter(e -> e.date.equals(LocalDate.parse("2024-05-11")))
						.count());
		assertEquals(3, modifications(repeatingDailyEndlessKey).size());
	}
}
