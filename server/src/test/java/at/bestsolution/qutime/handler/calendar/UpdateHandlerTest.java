package at.bestsolution.qutime.handler.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;

@QuarkusTest
public class UpdateHandlerTest extends CalendarHandlerTest<UpdateHandler> {

	@Inject
	public UpdateHandlerTest(UpdateHandler handler) {
		super(handler);
	}

	@Test
	public void testUpdateName() {
		var patch = Json.createPatchBuilder()
				.replace("name", "My Updated Calendar")
				.build();
		assertTrue(handler.update(handler_ownerlessCalendarKey, patch).isOk());
		assertEquals("My Updated Calendar", calendar(handler_ownerlessCalendarKey).name);
	}

	@Test
	public void testUpdateMulti() {
		var patch = Json.createPatchBuilder()
				.replace("name", "My Updated Calendar 2")
				.add("owner", "testowner@bestsolution.at")
				.build();
		assertNull(calendar(handler_ownerlessCalendarKey).owner);

		assertTrue(handler.update(handler_ownerlessCalendarKey, patch).isOk());

		assertEquals("My Updated Calendar 2", calendar(handler_ownerlessCalendarKey).name);
		assertEquals("testowner@bestsolution.at", calendar(handler_ownerlessCalendarKey).owner);
	}

	@Test
	public void testFail() {
		assertThrows(NullPointerException.class,
				() -> handler.update(null, null));
		assertThrows(NullPointerException.class,
				() -> handler.update(basicCalendarKey, null));
	}
}
