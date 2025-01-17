package at.bestsolution.quti.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.model.CalendarNew;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class CreateHandlerTest extends CalendarHandlerTest<CreateHandlerJPA> {

	@Inject
	public CreateHandlerTest(CreateHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void testCreate() {
		var dto = builderFactory.builder(CalendarNew.DataBuilder.class)
				.name("Test Handler")
				.owner("info@quti.dev")
				.build();
		var result = handler.create(builderFactory, dto);
		assertTrue(result.isOk());
		assertNotNull(result.value(), "Expected an UUID");
		assertDoesNotThrow(() -> UUID.fromString(result.value()), "Result should have been an UUID");
	}

	@Test
	public void testFail() {
		assertThrows(NullPointerException.class, () -> handler.create(builderFactory, null));
	}
}
