package at.bestsolution.qutime.handler.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.qutime.BaseHandlerTest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class GetHandlerTest extends BaseHandlerTest<GetHandler> {

	@Inject
	public GetHandlerTest(GetHandler handler) {
		super(handler);
	}

	@Test
	public void testGet() {
		var result = handler.get(basicCalendarKey);
		assertNotNull(result);
		assertEquals(basicCalendarKey, result.key());
	}

	@Test
	public void testFail() {
		assertThrows(NullPointerException.class, () -> handler.get(null));
	}
}
