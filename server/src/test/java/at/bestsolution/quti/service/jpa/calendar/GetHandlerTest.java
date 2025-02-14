package at.bestsolution.quti.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.InvalidArgumentException;
import at.bestsolution.quti.service.NotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class GetHandlerTest extends CalendarHandlerTest<GetHandlerJPA> {

	@Inject
	public GetHandlerTest(GetHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void testGet() {
		var result = handler.get(builderFactory, basicCalendarKey.toString());
		assertEquals(basicCalendarKey.toString(), result.key());
	}

	@Test
	public void testFail() {
		assertThrows(NullPointerException.class, () -> handler.get(builderFactory, null));
	}

	@Test
	public void testNotFound() {
		assertThrows(NotFoundException.class, () -> handler.get(builderFactory, UUID.randomUUID().toString()));
	}

	@Test
	public void testInvalidArgument() {
		assertThrows(InvalidArgumentException.class, () -> handler.get(builderFactory, "abcd"));
	}
}
