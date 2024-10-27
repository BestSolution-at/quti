package at.bestsolution.quti.handler.calendar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.dto.CalendarNewDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class CreateHandlerTest extends CalendarHandlerTest<CreateHandler> {

	@Inject
	public CreateHandlerTest(CreateHandler handler) {
		super(handler);
	}

	@Test
	public void testCreate() {
		var result = handler.create(new CalendarNewDTO("Test Handler", "info@quti.dev"));
		assertTrue(result.isOk());
		assertNotNull(result.value(), "Expected an UUID");
		assertDoesNotThrow(() -> UUID.fromString(result.value()), "Result should have been an UUID");
	}

	@Test
	public void testFail() {
		assertThrows(NullPointerException.class, () -> handler.create(null));
	}
}
