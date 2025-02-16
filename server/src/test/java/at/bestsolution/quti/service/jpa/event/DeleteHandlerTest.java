package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.InvalidArgumentException;
import at.bestsolution.quti.service.NotFoundException;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class DeleteHandlerTest extends EventHandlerTest<DeleteHandlerJPA> {

	public DeleteHandlerTest(DeleteHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.delete(builderFactory, "abcd", simpleEventKey.toString()));
	}

	@Test
	public void invalidEventKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.delete(builderFactory, basicCalendarKey.toString(), "abcd"));
	}

	@Test
	public void testNotFoundEventKey() {
		assertThrows(NotFoundException.class,
				() -> handler.delete(builderFactory, basicCalendarKey.toString(), UUID.randomUUID().toString()));
	}

	@Test
	public void testNotFoundCalendarKey() {
		assertThrows(NotFoundException.class,
				() -> handler.delete(builderFactory, UUID.randomUUID().toString(), simpleEventKey.toString()));
	}

	@Test
	public void testCalendarEventMismatch() {
		assertThrows(NotFoundException.class,
				() -> handler.delete(builderFactory, referenceCalendarKey.toString(), simpleEventKey.toString()));
	}

	@Test
	public void success() {
		handler.delete(builderFactory, basicCalendarKey.toString(), simpleEventKey.toString());
	}
}
