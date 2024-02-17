package at.bestsolution.qutime.handler.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.qutime.Utils.ResultType;
import at.bestsolution.qutime.dto.EventNewDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO.EventRepeatDailyDTO;
import at.bestsolution.qutime.dto.EventRepeatDTO.EventRepeatWeeklyDTO;
import at.bestsolution.qutime.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.qutime.model.repeat.EventRepeatWeeklyEntity;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CreateHandlerTest extends EventHandlerTest<CreateHandler> {

	public CreateHandlerTest(CreateHandler handler) {
		super(handler);
	}

	@Test
	public void testSingleCreate() {
		var newEvent = new EventNewDTO(
			"New event",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"), null);

		var eventKey = handler.create(ownerlessCalendarKey, newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertEquals(ownerlessCalendarKey, entity.calendar.key);
		assertEquals(newEvent.title(), entity.title);
		assertEquals(newEvent.description(), entity.description);
		assertEquals(newEvent.start().toInstant(), entity.start.toInstant());
		assertEquals(newEvent.end().toInstant(), entity.end.toInstant());
		assertNull(entity.repeatPattern);
	}

	@Test
	public void testNullStart() {
		var newEvent = new EventNewDTO(
			"New event",
			"New event description",
			null,
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"), null);

		var result = handler.create(ownerlessCalendarKey, newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testNullEnd() {
		var newEvent = new EventNewDTO(
			"New event",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			null, null);

		var result = handler.create(ownerlessCalendarKey, newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testNullTitle() {
		var newEvent = new EventNewDTO(
			null,
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"), null);

		var result = handler.create(ownerlessCalendarKey, newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testEmptyTitle() {
		var newEvent = new EventNewDTO(
			"",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"), null);

		var result = handler.create(ownerlessCalendarKey, newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testBlankTitle() {
		var newEvent = new EventNewDTO(
			"    ",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"), null);

		var result = handler.create(ownerlessCalendarKey, newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testRepeatingDailyEndless() {
		var repeat = EventRepeatDailyDTO.of((short)2, "Europe/Vienna", null);
		var newEvent = new EventNewDTO(
			"New event daily",
			"New event description daily",
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T14:00:00+01:00[Europe/Vienna]"), repeat);
		var eventKey = handler.create(ownerlessCalendarKey, newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertNotNull(entity.repeatPattern);
		assertInstanceOf(EventRepeatDailyEntity.class, entity.repeatPattern);
		assertEquals(repeat.interval, entity.repeatPattern.interval);
		assertEquals(ZoneId.of(repeat.timeZone), entity.repeatPattern.recurrenceTimezone);
		assertNull(entity.repeatPattern.endDate);
	}

	@Test
	public void testRepeatingDailyEnd() {
		var repeat = EventRepeatDailyDTO.of((short)2, "Europe/Vienna", LocalDate.parse("2020-01-31"));
		var newEvent = new EventNewDTO(
			"New event daily with end",
			"New event description daily with end",
			ZonedDateTime.parse("2020-01-01T14:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"), repeat);
		var eventKey = handler.create(ownerlessCalendarKey, newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertInstanceOf(EventRepeatDailyEntity.class, entity.repeatPattern);
		assertEquals(ZonedDateTime.parse("2020-02-01T00:00:00+01:00[Europe/Vienna]").toInstant(), entity.repeatPattern.endDate.toInstant());
	}

	@Test
	public void testRepeatingWeeklyEndless() {
		var repeat = EventRepeatWeeklyDTO.of((short)1, "Europe/Vienna", null, List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
		var newEvent = new EventNewDTO(
			"New event weekly with end",
			"New event description weekly with end",
			ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T18:00:00+01:00[Europe/Vienna]"), repeat);
		var eventKey = handler.create(ownerlessCalendarKey, newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertInstanceOf(EventRepeatWeeklyEntity.class, entity.repeatPattern);
		assertNotNull(entity.end);
		assertNull(entity.repeatPattern.endDate);
		assertEquals(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY), ((EventRepeatWeeklyEntity)entity.repeatPattern).daysOfWeek);
	}

	@Test
	public void testRepeatingWeeklyEnd() {
		var repeat = EventRepeatWeeklyDTO.of((short)1, "Europe/Vienna", LocalDate.parse("2020-01-15"), List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
		var newEvent = new EventNewDTO(
			"New event weekly with end",
			"New event description weekly with end",
			ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T18:00:00+01:00[Europe/Vienna]"), repeat);
		var eventKey = handler.create(ownerlessCalendarKey, newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertInstanceOf(EventRepeatWeeklyEntity.class, entity.repeatPattern);
		assertEquals(ZonedDateTime.parse("2020-01-16T00:00:00+01:00[Europe/Vienna]").toInstant(), entity.repeatPattern.endDate.toInstant());
		assertEquals(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY), ((EventRepeatWeeklyEntity)entity.repeatPattern).daysOfWeek);
	}
}
