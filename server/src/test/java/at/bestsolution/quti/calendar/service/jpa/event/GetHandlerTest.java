package at.bestsolution.quti.calendar.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZoneId;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.calendar.service.InvalidArgumentException;
import at.bestsolution.quti.calendar.service.NotFoundException;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GetHandlerTest extends EventHandlerTest<GetHandlerJPA> {

	public GetHandlerTest(GetHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.get(builderFactory, "abcd", simpleEventKey.toString(), null));
	}

	@Test
	public void invalidEventKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.get(builderFactory, basicCalendarKey.toString(), "abcd", null));
	}

	@Test
	public void testNoTimezone() {
		var result = handler.get(builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(), null);
		assertNotNull(result);
		assertEquals("2024-01-10T06:00Z", result.start().toString());
		assertEquals("2024-01-10T12:00Z", result.end().toString());
	}

	@Test
	public void testEuropeVienna() {
		var result = handler.get(builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(),
				ZoneId.of("Europe/Vienna"));
		assertNotNull(result);
		assertEquals("2024-01-10T07:00+01:00[Europe/Vienna]", result.start().toString());
		assertEquals("2024-01-10T13:00+01:00[Europe/Vienna]", result.end().toString());
	}

	@Test
	public void testUSWestcoast() {
		var result = handler.get(builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(),
				ZoneId.of("America/Los_Angeles"));
		assertNotNull(result);
		assertEquals("2024-01-09T22:00-08:00[America/Los_Angeles]", result.start().toString());
		assertEquals("2024-01-10T04:00-08:00[America/Los_Angeles]", result.end().toString());
	}

	@Test
	public void testNotFoundEventKey() {
		assertThrows(NotFoundException.class,
				() -> handler.get(builderFactory, basicCalendarKey.toString(), UUID.randomUUID().toString(), null));
	}

	@Test
	public void testNotFoundCalendarKey() {
		assertThrows(NotFoundException.class,
				() -> handler.get(builderFactory, UUID.randomUUID().toString(), simpleEventKey.toString(), null));
	}

	@Test
	public void testCalendarEventMismatch() {
		assertThrows(NotFoundException.class,
				() -> handler.get(builderFactory, referenceCalendarKey.toString(), simpleEventKey.toString(), null));
	}
}
