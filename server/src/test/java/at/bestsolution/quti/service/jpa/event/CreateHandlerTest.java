package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.model.repeat.EventRepeatWeeklyEntity;
import at.bestsolution.quti.rest.dto.EventNewDTOImpl;
import at.bestsolution.quti.service.Result.ResultType;
import at.bestsolution.quti.service.dto.EventNewDTO;
import at.bestsolution.quti.service.jpa.event.utils.EventRepeatDTOUtil;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CreateHandlerTest extends EventHandlerTest<CreateHandlerJPA> {

	public CreateHandlerTest(CreateHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		var newEvent = new EventNewDTOImpl(
			"New event",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of(),
			List.of());

		var result = handler.create(builderFactory, "abcd", newEvent);
		assertFalse(result.isOk());
		assertEquals(result.type(), ResultType.INVALID_PARAMETER);
	}

	@Test
	public void testSingleCreate() {
		var newEvent = new EventNewDTOImpl(
			"New event",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of(),
			List.of());

		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertEquals(ownerlessCalendarKey, entity.calendar.key);
		assertEquals(newEvent.title(), entity.title);
		assertEquals(newEvent.description(), entity.description);
		assertEquals(newEvent.start().toInstant(), entity.start.toInstant());
		assertEquals(newEvent.end().toInstant(), entity.end.toInstant());
		assertNull(entity.repeatPattern);
	}

	@Test
	public void testCreateWithTags() {
		var newEvent = new EventNewDTOImpl(
			"New event with tags",
			"New event with tags description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of("tag-1","tag-2"),
			List.of());

		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertEquals(ownerlessCalendarKey, entity.calendar.key);
		assertEquals(newEvent.title(), entity.title);
		assertEquals(newEvent.description(), entity.description);
		assertEquals(newEvent.start().toInstant(), entity.start.toInstant());
		assertEquals(newEvent.end().toInstant(), entity.end.toInstant());
		assertEquals(List.of("tag-1", "tag-2"), entity.tags);
		assertNull(entity.repeatPattern);
	}


	@Test
	public void testNullStart() {
		var newEvent = new EventNewDTOImpl(
			"New event",
			"New event description",
			null,
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of(),
			List.of());

		var result = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testNullEnd() {
		var newEvent = new EventNewDTOImpl(
			"New event",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			null,
			false,
			null,
			List.of(),
			List.of());

		var result = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testNullTitle() {
		var newEvent = new EventNewDTOImpl(
			null,
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of(),
			List.of()
		);

		var result = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testEmptyTitle() {
		var newEvent = new EventNewDTOImpl(
			"",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of(),
			List.of());

		var result = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testBlankTitle() {
		var newEvent = new EventNewDTOImpl(
			"    ",
			"New event description",
			ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of(),
			List.of());

		var result = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		assertFalse(result.isOk());
		assertEquals(ResultType.INVALID_CONTENT, result.type());
	}

	@Test
	public void testWithReferences() {
		var newEvent = new EventNewDTOImpl(
			"Event referenced",
			"New referenced event description",
			ZonedDateTime.parse("2020-01-01T08:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T09:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of(),
			List.of(writeableReferenceCalendarKey.toString()));

		var result = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		assertTrue(result.isOk());
		var eventEntity = event(UUID.fromString(result.value()));

		assertEquals(1,eventEntity.references.size());
		assertEquals(writeableReferenceCalendarKey, eventEntity.references.get(0).calendar.key);
	}

	@Test
	public void testWithInvalidReferences() {
		var newEvent = new EventNewDTOImpl(
			"Event referenced",
			"New referenced event description",
			ZonedDateTime.parse("2020-01-01T08:00:00+01:00[Europe/Vienna]"),
			ZonedDateTime.parse("2020-01-01T09:00:00+01:00[Europe/Vienna]"),
			false,
			null,
			List.of(),
			List.of(writeableReferenceCalendarKey.toString(), UUID.randomUUID().toString()));

		var result = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		assertFalse(result.isOk());
	}

	@Test
	public void testRepeatingDailyEndless() {
		var repeat = EventRepeatDTOUtil.EventRepeatDailyDTOUtil.of(builderFactory, (short)2, "Europe/Vienna", null);
		var newEvent = builderFactory.builder(EventNewDTO.Builder.class)
			.title("New event daily")
			.description("New event description daily")
			.start(ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"))
			.end(ZonedDateTime.parse("2020-01-01T14:00:00+01:00[Europe/Vienna]"))
			.fullday(false)
			.repeat(repeat)
			.tags(List.of())
			.referencedCalendars(List.of())
			.build();
		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertNotNull(entity.repeatPattern);
		assertInstanceOf(EventRepeatDailyEntity.class, entity.repeatPattern);
		assertEquals(repeat.interval(), entity.repeatPattern.interval);
		assertEquals(repeat.timeZone(), entity.repeatPattern.recurrenceTimezone);
		assertNull(entity.repeatPattern.endDate);
	}

	@Test
	public void testRepeatingDailyEnd() {
		var repeat = EventRepeatDTOUtil.EventRepeatDailyDTOUtil.of(builderFactory, (short)2, "Europe/Vienna", LocalDate.parse("2020-01-31"));
		var newEvent = builderFactory.builder(EventNewDTO.Builder.class)
			.title("New event daily with end")
			.description("New event description daily with end")
			.start(ZonedDateTime.parse("2020-01-01T14:00:00+01:00[Europe/Vienna]"))
			.end(ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"))
			.fullday(false)
			.repeat(repeat)
			.tags(List.of())
			.referencedCalendars(List.of())
			.build();

		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertInstanceOf(EventRepeatDailyEntity.class, entity.repeatPattern);
		assertEquals(ZonedDateTime.parse("2020-01-31T23:59:59+01:00[Europe/Vienna]").toInstant(), entity.repeatPattern.endDate.toInstant());
	}

	@Test
	public void testRepeatingWeeklyEndless() {
		var repeat = EventRepeatDTOUtil.EventRepeatWeeklyDTOUtil.of(builderFactory, (short)1, "Europe/Vienna", null, List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
		var newEvent = builderFactory.builder(EventNewDTO.Builder.class)
			.title("New event weekly with end")
			.description("New event description weekly with end")
			.start(ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"))
			.end(ZonedDateTime.parse("2020-01-01T18:00:00+01:00[Europe/Vienna]"))
			.fullday(false)
			.repeat(repeat)
			.tags(List.of())
			.referencedCalendars(List.of())
			.build();
		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertInstanceOf(EventRepeatWeeklyEntity.class, entity.repeatPattern);
		assertNotNull(entity.end);
		assertNull(entity.repeatPattern.endDate);
		assertEquals(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY), ((EventRepeatWeeklyEntity)entity.repeatPattern).daysOfWeek);
	}

	@Test
	public void testRepeatingWeeklyEnd() {
		var repeat = EventRepeatDTOUtil.EventRepeatWeeklyDTOUtil.of(builderFactory, (short)1, "Europe/Vienna", LocalDate.parse("2020-01-15"), List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
		var newEvent = builderFactory.builder(EventNewDTO.Builder.class)
			.title("New event weekly with end")
			.description("New event description weekly with end")
			.start(ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"))
			.end(ZonedDateTime.parse("2020-01-01T18:00:00+01:00[Europe/Vienna]"))
			.fullday(false)
			.repeat(repeat)
			.tags(List.of())
			.referencedCalendars(List.of())
			.build();

		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);
		var entity = event(UUID.fromString(eventKey.value()));

		assertInstanceOf(EventRepeatWeeklyEntity.class, entity.repeatPattern);
		assertEquals(ZonedDateTime.parse("2020-01-15T23:59:59+01:00[Europe/Vienna]").toInstant(), entity.repeatPattern.endDate.toInstant());
		assertEquals(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY), ((EventRepeatWeeklyEntity)entity.repeatPattern).daysOfWeek);
	}

	@Test
	public void testInvalidInterval() {
		var repeat = EventRepeatDTOUtil.EventRepeatWeeklyDTOUtil.of(builderFactory, (short)0, "Europe/Vienna", LocalDate.parse("2020-01-15"), List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
		var newEvent = builderFactory.builder(EventNewDTO.Builder.class)
			.title("New event weekly with end")
			.description("New event description weekly with end")
			.start(ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"))
			.end(ZonedDateTime.parse("2020-01-01T18:00:00+01:00[Europe/Vienna]"))
			.fullday(false)
			.repeat(repeat)
			.tags(List.of())
			.referencedCalendars(List.of())
			.build();
		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);

		assertFalse(eventKey.isOk());
	}

	@Test
	public void testInvalidWeek() {
		var repeat = EventRepeatDTOUtil.EventRepeatWeeklyDTOUtil.of(builderFactory, (short)1, "Europe/Vienna", LocalDate.parse("2020-01-15"), List.of());
		var newEvent = builderFactory.builder(EventNewDTO.Builder.class)
			.title("New event weekly with end")
			.description("New event description weekly with end")
			.start(ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"))
			.end(ZonedDateTime.parse("2020-01-01T18:00:00+01:00[Europe/Vienna]"))
			.fullday(false)
			.repeat(repeat)
			.tags(List.of())
			.referencedCalendars(List.of())
			.build();
		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent);

		assertFalse(eventKey.isOk());
	}
}
