package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class EndRepeatingHandlerTest extends EventHandlerTest<EndRepeatingHandlerJPA> {

	@Inject
	public EndRepeatingHandlerTest(EndRepeatingHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		var result = handler.endRepeat(builderFactory, "abcd", simpleEventKey.toString(), null);
		assertFalse(result.isOk());
	}

	@Test
	public void invalidEventKey() {
		var result = handler.endRepeat(builderFactory, basicCalendarKey.toString(), "abcd", null);
		assertFalse(result.isOk());
	}

	@Test
	public void endRepeating() {
		var newEnd = LocalDate.parse("2024-01-10");
		var result = handler.endRepeat(
			builderFactory,
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString(),
			newEnd
		);

		assertTrue(result.isOk());

		var end = event(repeatingDailyEndlessKey)
			.repeatPattern.endDate;
		assertNotNull(end);
		assertEquals(end.toLocalDate(), newEnd);
	}

	@Test
	public void invalidEnd() {
		var result = handler.endRepeat(
			builderFactory,
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString(),
			LocalDate.parse("2023-12-31")
		);
		assertFalse(result.isOk());
	}
}
