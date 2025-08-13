package at.bestsolution.quti.calendar.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.calendar.service.InvalidContentException;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.calendar.service.model.Event;
import at.bestsolution.quti.calendar.service.model.EventRepeatDaily;
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
		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.title("Patched Title")
				.build();

		handler.update(this.builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(), dto);
		assertEquals("Patched Title", event(simpleEventKey).title);
		assertEquals("A simple none repeating event",
				event(simpleEventKey).description);
	}

	@Test
	public void updateSimpleEventTitle_Empty() {
		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.title("")
				.build();

		assertThrows(InvalidContentException.class,
				() -> handler.update(this.builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(), dto));
		assertEquals("Simple Event", event(simpleEventKey).title);
		assertEquals("A simple none repeating event",
				event(simpleEventKey).description);
	}

	@Test
	public void updateSimpleEventDescription() {
		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.description("Patched Description")
				.build();

		handler.update(this.builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(), dto);
		assertEquals("Simple Event", event(simpleEventKey).title);
		assertEquals("Patched Description", event(simpleEventKey).description);
	}

	@Test
	public void updateSimpleEventStart() {
		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.start(ZonedDateTime.parse("2024-01-10T06:00:00+01:00[Europe/Vienna]"))
				.build();

		handler.update(this.builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(), dto);
		var entity = event(simpleEventKey);
		assertEquals(entity.start,
				ZonedDateTime.parse("2024-01-10T06:00:00+01:00[Europe/Vienna]").withZoneSameInstant(ZoneOffset.UTC));
	}

	@Test
	public void updateSimpleEventEnd() {
		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.end(ZonedDateTime.parse("2024-01-10T14:00:00+01:00[Europe/Vienna]"))
				.build();

		handler.update(this.builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(), dto);
		var entity = event(simpleEventKey);
		assertEquals(entity.end,
				ZonedDateTime.parse("2024-01-10T14:00:00+01:00[Europe/Vienna]").withZoneSameInstant(ZoneOffset.UTC));
	}

	@Test
	public void updateRepeatEventStart() {
		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.start(ZonedDateTime.parse("2024-01-01T12:00:00+01:00[Europe/Vienna]"))
				.build();

		handler.update(this.builderFactory, basicCalendarKey.toString(), repeatingDailyEndlessKey.toString(), dto);

		var entity = event(repeatingDailyEndlessKey);
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(entity.start,
				ZonedDateTime.parse("2024-01-01T12:00:00+01:00[Europe/Vienna]").withZoneSameInstant(ZoneOffset.UTC));
		assertEquals(3, modifications.size());
	}

	@Test
	public void updateSetNewRepeat() {
		var repeat = builderFactory.builder(EventRepeatDaily.DataBuilder.class)
				.interval((short) 7)
				.timeZone(ZoneId.of("Europe/Vienna"))
				.build();

		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.repeat(repeat)
				.build();

		handler.update(this.builderFactory, basicCalendarKey.toString(), simpleEventKey.toString(), dto);
		var entity = event(simpleEventKey);
		assertNotNull(entity.repeatPattern);
		assertTrue(entity.repeatPattern instanceof EventRepeatDailyEntity);
		assertEquals(7, entity.repeatPattern.interval);
	}

	@Test
	public void updateRemoveRepeat() {
		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.repeat(null)
				.build();
		handler.update(this.builderFactory, basicCalendarKey.toString(), repeatingDailyEndlessKey.toString(), dto);
		assertNull(event(repeatingDailyEndlessKey).repeatPattern);
		assertEquals(0, modifications(repeatingDailyEndlessKey).size());
	}

	@Test
	public void updateReferencedCalendars() {
		var dto = builderFactory.builder(Event.PatchBuilder.class)
				.referencedCalendars(List.of(referenceCalendarKey.toString()))
				.build();
		handler.update(builderFactory, basicCalendarKey.toString(), repeatingDailyEndlessKey.toString(), dto);
		assertEquals(event(repeatingDailyEndlessKey).references.size(), 1);
		assertEquals(event(repeatingDailyEndlessKey).references.get(0).calendar.key, referenceCalendarKey);
	}

	// @Test
	// public void updateEndRepeat() {
	// var repeat = new EventRepeatDailyPatchDTOImpl();
	// repeat.setEndDate(LocalDate.parse("2025-01-01"));
	// dto.setRepeat(repeat);

	// var dto = builderFactory.builder(Event.PatchBuilder.class)
	// .build();

	// var rv = handler.update(basicCalendarKey, repeatingDailyEndlessKey, dto);
	// assertTrue(rv.isOk());
	// var entity = event(repeatingDailyEndlessKey);
	// assertNotNull(entity.repeatPattern);
	// assertNotNull(entity.repeatPattern.endDate);
	// assertEquals(ZonedDateTime.parse("2025-01-01T22:59:59Z"),
	// entity.repeatPattern.endDate.withZoneSameInstant(ZoneOffset.UTC));
	// assertEquals(3, modifications(repeatingDailyEndlessKey).size());
	// }

	// @Test
	// public void updateInterval() {
	// var dto = new EventPatchDTOImpl();
	// dto.setKey(repeatingDailyEndlessKey.toString());
	// var repeat = new EventRepeatDailyPatchDTOImpl();
	// repeat.setInterval((short) 2);
	// dto.setRepeat(repeat);

	// var rv = handler.update(basicCalendarKey, repeatingDailyEndlessKey, dto);
	// assertTrue(rv.isOk());
	// var entity = event(repeatingDailyEndlessKey);
	// assertNotNull(entity.repeatPattern);
	// assertEquals(2, entity.repeatPattern.interval);
	// }
}
