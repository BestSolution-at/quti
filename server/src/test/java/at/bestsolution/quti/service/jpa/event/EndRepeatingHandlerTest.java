package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.InvalidArgumentException;
import at.bestsolution.quti.service.InvalidContentException;
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
		assertThrows(InvalidArgumentException.class,
				() -> handler.endRepeat(builderFactory, "abcd", simpleEventKey.toString(), null));
	}

	@Test
	public void invalidEventKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.endRepeat(builderFactory, basicCalendarKey.toString(), "abcd", null));
	}

	@Test
	public void endRepeating() {
		var newEnd = LocalDate.parse("2024-01-10");
		handler.endRepeat(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString(),
				newEnd);

		var end = event(repeatingDailyEndlessKey).repeatPattern.endDate;
		assertNotNull(end);
		assertEquals(end.toLocalDate(), newEnd);
	}

	@Test
	public void invalidEnd() {
		assertThrows(InvalidContentException.class, () -> handler.endRepeat(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString(),
				LocalDate.parse("2023-12-31")));
	}
}
