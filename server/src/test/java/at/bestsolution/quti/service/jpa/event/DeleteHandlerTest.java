package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.Result.ResultType;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class DeleteHandlerTest extends EventHandlerTest<DeleteHandlerJPA> {

	public DeleteHandlerTest(DeleteHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		var result = handler.delete("abcd", simpleEventKey.toString());
		assertFalse(result.isOk());
	}

	@Test
	public void invalidEventKey() {
		var result = handler.delete(basicCalendarKey.toString(), "abcd");
		assertFalse(result.isOk());
	}

	@Test
	public void testNotFoundEventKey() {
		var result = handler.delete(basicCalendarKey.toString(), UUID.randomUUID().toString());
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}

	@Test
	public void testNotFoundCalendarKey() {
		var result = handler.delete(UUID.randomUUID().toString(), simpleEventKey.toString());
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}

	@Test
	public void testCalendarEventMismatch() {
		var result = handler.delete(referenceCalendarKey.toString(), simpleEventKey.toString());
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}

	@Test
	public void success() {
		var result = handler.delete(basicCalendarKey.toString(), simpleEventKey.toString());
		assertTrue(result.isOk());
	}
}
