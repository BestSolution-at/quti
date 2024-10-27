package at.bestsolution.quti.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
		var result = handler.get(basicCalendarKey.toString());
		assertTrue(result.isOk());
		assertEquals(basicCalendarKey.toString(), result.value().key());
	}

	@Test
	public void testFail() {
		assertThrows(NullPointerException.class, () -> handler.get(null));
	}
}
