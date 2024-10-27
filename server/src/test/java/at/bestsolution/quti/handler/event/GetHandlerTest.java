package at.bestsolution.quti.handler.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.Result.ResultType;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GetHandlerTest extends EventHandlerTest<GetHandler> {

	public GetHandlerTest(GetHandler handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		var result = handler.get("abcd", simpleEventKey.toString(), null);
		assertFalse(result.isOk());
	}

	@Test
	public void invalidEventKey() {
		var result = handler.get(basicCalendarKey.toString(), "abcd", null);
		assertFalse(result.isOk());
	}

	@Test
	public void testNoTimezone() {
		var result = handler.get(basicCalendarKey.toString(), simpleEventKey.toString(), null);
		assertTrue(result.isOk());
		assertNotNull(result.value());
		assertEquals("2024-01-10T06:00Z", result.value().start().toString());
		assertEquals("2024-01-10T12:00Z", result.value().end().toString());
	}

	@Test
	public void testEuropeVienna() {
		var result = handler.get(basicCalendarKey.toString(), simpleEventKey.toString(), ZoneId.of("Europe/Vienna"));
		assertTrue(result.isOk());
		assertNotNull(result.value());
		assertEquals("2024-01-10T07:00+01:00[Europe/Vienna]", result.value().start().toString());
		assertEquals("2024-01-10T13:00+01:00[Europe/Vienna]", result.value().end().toString());
	}

	@Test
	public void testUSWestcoast() {
		var result = handler.get(basicCalendarKey.toString(), simpleEventKey.toString(), ZoneId.of("America/Los_Angeles"));
		assertTrue(result.isOk());
		assertNotNull(result.value());
		assertEquals("2024-01-09T22:00-08:00[America/Los_Angeles]", result.value().start().toString());
		assertEquals("2024-01-10T04:00-08:00[America/Los_Angeles]", result.value().end().toString());
	}

	@Test
	public void testNotFoundEventKey() {
		var result = handler.get(basicCalendarKey.toString(), UUID.randomUUID().toString(), null);
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}

	@Test
	public void testNotFoundCalendarKey() {
		var result = handler.get(UUID.randomUUID().toString(), simpleEventKey.toString(), null);
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}

	@Test
	public void testCalendarEventMismatch() {
		var result = handler.get(referenceCalendarKey.toString(), simpleEventKey.toString(), null);
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}
}
