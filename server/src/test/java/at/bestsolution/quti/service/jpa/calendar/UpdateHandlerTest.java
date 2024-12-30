package at.bestsolution.quti.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.rest.dto.CalendarPatchDTOImpl;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class UpdateHandlerTest extends CalendarHandlerTest<UpdateHandlerJPA> {

	@Inject
	public UpdateHandlerTest(UpdateHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void testUpdateName() {
		var patch = new CalendarPatchDTOImpl();
		patch.setKey(handler_ownerlessCalendarKey.toString());
		patch.setName("My Updated Calendar");

		assertTrue(handler.update(builderFactory, handler_ownerlessCalendarKey.toString(), patch).isOk());
		assertEquals("My Updated Calendar", calendar(handler_ownerlessCalendarKey).name);
	}

	@Test
	public void testUpdateMulti() {
		var patch = new CalendarPatchDTOImpl();
		patch.setKey(handler_ownerlessCalendarKey.toString());
		patch.setName("My Updated Calendar 2");
		patch.setOwner("testowner@bestsolution.at");

		assertNull(calendar(handler_ownerlessCalendarKey).owner);

		assertTrue(handler.update(builderFactory, handler_ownerlessCalendarKey.toString(), patch).isOk());

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
