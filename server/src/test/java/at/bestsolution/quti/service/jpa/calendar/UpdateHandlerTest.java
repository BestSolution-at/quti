package at.bestsolution.quti.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonPatch;

@QuarkusTest
public class UpdateHandlerTest extends CalendarHandlerTest<UpdateHandlerJPA> {

	@Inject
	public UpdateHandlerTest(UpdateHandlerJPA handler) {
		super(handler);
	}

	private static String toString(JsonPatch patch) {
		var w = new StringWriter();
		var writer = Json.createWriter(w);
		writer.writeArray(patch.toJsonArray());
		writer.close();
		return w.toString();
	}

	@Test
	public void testUpdateName() {
		var patch = Json.createPatchBuilder()
				.replace("name", "My Updated Calendar")
				.build();
		assertTrue(handler.update(builderFactory, handler_ownerlessCalendarKey.toString(), toString(patch)).isOk());
		assertEquals("My Updated Calendar", calendar(handler_ownerlessCalendarKey).name);
	}

	@Test
	public void testUpdateMulti() {
		var patch = Json.createPatchBuilder()
				.replace("name", "My Updated Calendar 2")
				.add("owner", "testowner@bestsolution.at")
				.build();
		assertNull(calendar(handler_ownerlessCalendarKey).owner);

		assertTrue(handler.update(builderFactory, handler_ownerlessCalendarKey.toString(), toString(patch)).isOk());

		assertEquals("My Updated Calendar 2", calendar(handler_ownerlessCalendarKey).name);
		assertEquals("testowner@bestsolution.at", calendar(handler_ownerlessCalendarKey).owner);
	}

	@Test
	public void testFail() {
		assertThrows(NullPointerException.class,
				() -> handler.update(builderFactory, null, null));
		assertThrows(NullPointerException.class,
				() -> handler.update(builderFactory, basicCalendarKey.toString(), null));
	}
}
