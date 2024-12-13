package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.rest.dto.EventPatchDTOImpl;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class UpdateHandlerTest extends EventHandlerTest<UpdateHandlerJPA> {
	@Inject
	public UpdateHandlerTest(UpdateHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void updateSimpleEventTitle() {
		var dto = new EventPatchDTOImpl(simpleEventKey.toString());
		dto.setTitle("Patched Title");

		var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
		assertTrue(rv.isOk());
		assertEquals("Patched Title", event(simpleEventKey).title);
		assertEquals("A simple none repeating event", event(simpleEventKey).description);
	}

	@Test
	public void updateSimpleEventTitle_Empty() {
		var dto = new EventPatchDTOImpl(simpleEventKey.toString());
		dto.setTitle("");

		var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
		assertTrue(rv.isNotOk());
		assertEquals("Simple Event", event(simpleEventKey).title);
		assertEquals("A simple none repeating event", event(simpleEventKey).description);
	}

	@Test
	public void updateSimpleEventDescription() {
		var dto = new EventPatchDTOImpl(simpleEventKey.toString());
		dto.setDescription("Patched Description");

		var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
		assertTrue(rv.isOk());
		assertEquals("Simple Event", event(simpleEventKey).title);
		assertEquals("Patched Description", event(simpleEventKey).description);
	}
}
