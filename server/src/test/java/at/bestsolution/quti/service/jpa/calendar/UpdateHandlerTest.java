package at.bestsolution.quti.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.model.Calendar;
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
		var patch = builderFactory.builder(Calendar.PatchBuilder.class)
				.name("My Updated Calendar")
				.build();

		handler.update(builderFactory, handler_ownerlessCalendarKey.toString(), patch);
		assertEquals("My Updated Calendar",
				calendar(handler_ownerlessCalendarKey).name);
	}

	@Test
	public void testUpdateMulti() {
		var patch = builderFactory.builder(Calendar.PatchBuilder.class)
				.name("My Updated Calendar 2")
				.owner("testowner@bestsolution.at")
				.build();

		assertNull(calendar(handler_ownerlessCalendarKey).owner);

		handler.update(builderFactory, handler_ownerlessCalendarKey.toString(), patch);

		assertEquals("My Updated Calendar 2",
				calendar(handler_ownerlessCalendarKey).name);
		assertEquals("testowner@bestsolution.at",
				calendar(handler_ownerlessCalendarKey).owner);
	}

	@Test
	public void testFail() {
		assertThrows(NullPointerException.class,
				() -> handler.update(builderFactory, null, null));
		assertThrows(NullPointerException.class,
				() -> handler.update(builderFactory, basicCalendarKey.toString(), null));
	}
}
