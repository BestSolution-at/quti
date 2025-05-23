package at.bestsolution.quti.calendar.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatDailyEntity;
import at.bestsolution.quti.calendar.service.jpa.model.repeat.EventRepeatWeeklyEntity;
import at.bestsolution.quti.calendar.service.model.EventNew;
import at.bestsolution.quti.calendar.service.InvalidArgumentException;
import at.bestsolution.quti.calendar.service.InvalidContentException;
import at.bestsolution.quti.calendar.service.jpa.event.utils.EventRepeatDTOUtil;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CreateHandlerTest extends EventHandlerTest<CreateHandlerJPA> {

	public CreateHandlerTest(CreateHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("New event")
				.description("New event description")
				.start(ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();

		assertThrows(InvalidArgumentException.class, () -> handler.create(builderFactory, "abcd", dto));
	}

	@Test
	public void testSingleCreate() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("New event")
				.description("New event description")
				.start(ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();

		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), dto);
		var entity = event(UUID.fromString(eventKey));

		assertEquals(ownerlessCalendarKey, entity.calendar.key);
		assertEquals(dto.title(), entity.title);
		assertEquals(dto.description(), entity.description);
		assertEquals(dto.start().toInstant(), entity.start.toInstant());
		assertEquals(dto.end().toInstant(), entity.end.toInstant());
		assertNull(entity.repeatPattern);
	}

	@Test
	public void testCreateWithTags() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("New event with tags")
				.description("New event with tags description")
				.start(ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of("tag-1", "tag-2"))
				.referencedCalendars(List.of())
				.build();

		var eventKey = handler.create(builderFactory, ownerlessCalendarKey.toString(), dto);
		var entity = event(UUID.fromString(eventKey));

		assertEquals(ownerlessCalendarKey, entity.calendar.key);
		assertEquals(dto.title(), entity.title);
		assertEquals(dto.description(), entity.description);
		assertEquals(dto.start().toInstant(), entity.start.toInstant());
		assertEquals(dto.end().toInstant(), entity.end.toInstant());
		assertEquals(List.of("tag-1", "tag-2"), entity.tags);
		assertNull(entity.repeatPattern);
	}

	@Test
	@Disabled
	public void testNullStart() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("New event")
				.description("New event description")
				// .start(null)
				.end(ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();

		assertThrows(InvalidContentException.class, () -> handler.create(builderFactory, ownerlessCalendarKey.toString(),
				dto));
	}

	@Test
	@Disabled
	public void testNullEnd() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("New event")
				.description("New event description")
				.start(ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"))
				// .end(null)
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();

		assertThrows(InvalidContentException.class, () -> handler.create(builderFactory, ownerlessCalendarKey.toString(),
				dto));
	}

	@Test
	@Disabled
	public void testNullTitle() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				// .title(null)
				.description("New event description")
				.start(ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();

		assertThrows(InvalidContentException.class, () -> handler.create(builderFactory, ownerlessCalendarKey.toString(),
				dto));
	}

	@Test
	public void testEmptyTitle() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("")
				.description("New event description")
				.start(ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();

		assertThrows(InvalidContentException.class,
				() -> handler.create(builderFactory, ownerlessCalendarKey.toString(), dto));
	}

	@Test
	public void testBlankTitle() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("    ")
				.description("New event description")
				.start(ZonedDateTime.parse("2020-01-01T10:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T12:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();

		assertThrows(InvalidContentException.class,
				() -> handler.create(builderFactory, ownerlessCalendarKey.toString(), dto));
	}

	@Test
	public void testWithReferences() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("Event referenced")
				.description("New referenced event description")
				.start(ZonedDateTime.parse("2020-01-01T08:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T09:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of(writeableReferenceCalendarKey.toString()))
				.build();

		var result = handler.create(builderFactory, ownerlessCalendarKey.toString(), dto);
		var eventEntity = event(UUID.fromString(result));

		assertEquals(1, eventEntity.references.size());
		assertEquals(writeableReferenceCalendarKey, eventEntity.references.get(0).calendar.key);
	}

	@Test
	public void testWithInvalidReferences() {
		var dto = builderFactory.builder(EventNew.DataBuilder.class)
				.title("Event referenced")
				.description("New referenced event description")
				.start(ZonedDateTime.parse("2020-01-01T08:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T09:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(null)
				.tags(List.of())
				.referencedCalendars(List.of(writeableReferenceCalendarKey.toString(), UUID.randomUUID().toString()))
				.build();

		assertThrows(InvalidContentException.class,
				() -> handler.create(builderFactory, ownerlessCalendarKey.toString(), dto));
	}

	@Test
	public void testRepeatingDailyEndless() {
		var repeat = EventRepeatDTOUtil.EventRepeatDailyDTOUtil.of(builderFactory, (short) 2, "Europe/Vienna", null);
		var newEvent = builderFactory.builder(EventNew.DataBuilder.class)
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
		var entity = event(UUID.fromString(eventKey));

		assertNotNull(entity.repeatPattern);
		assertInstanceOf(EventRepeatDailyEntity.class, entity.repeatPattern);
		assertEquals(repeat.interval(), entity.repeatPattern.interval);
		assertEquals(repeat.timeZone(), entity.repeatPattern.recurrenceTimezone);
		assertNull(entity.repeatPattern.endDate);
	}

	@Test
	public void testRepeatingDailyEnd() {
		var repeat = EventRepeatDTOUtil.EventRepeatDailyDTOUtil.of(builderFactory, (short) 2, "Europe/Vienna",
				LocalDate.parse("2020-01-31"));
		var newEvent = builderFactory.builder(EventNew.DataBuilder.class)
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
		var entity = event(UUID.fromString(eventKey));

		assertInstanceOf(EventRepeatDailyEntity.class, entity.repeatPattern);
		assertEquals(ZonedDateTime.parse("2020-01-31T23:59:59+01:00[Europe/Vienna]").toInstant(),
				entity.repeatPattern.endDate.toInstant());
	}

	@Test
	public void testRepeatingWeeklyEndless() {
		var repeat = EventRepeatDTOUtil.EventRepeatWeeklyDTOUtil.of(builderFactory, (short) 1, "Europe/Vienna", null,
				List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
		var newEvent = builderFactory.builder(EventNew.DataBuilder.class)
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
		var entity = event(UUID.fromString(eventKey));

		assertInstanceOf(EventRepeatWeeklyEntity.class, entity.repeatPattern);
		assertNotNull(entity.end);
		assertNull(entity.repeatPattern.endDate);
		assertEquals(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY),
				((EventRepeatWeeklyEntity) entity.repeatPattern).daysOfWeek);
	}

	@Test
	public void testRepeatingWeeklyEnd() {
		var repeat = EventRepeatDTOUtil.EventRepeatWeeklyDTOUtil.of(builderFactory, (short) 1, "Europe/Vienna",
				LocalDate.parse("2020-01-15"), List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
		var newEvent = builderFactory.builder(EventNew.DataBuilder.class)
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
		var entity = event(UUID.fromString(eventKey));

		assertInstanceOf(EventRepeatWeeklyEntity.class, entity.repeatPattern);
		assertEquals(ZonedDateTime.parse("2020-01-15T23:59:59+01:00[Europe/Vienna]").toInstant(),
				entity.repeatPattern.endDate.toInstant());
		assertEquals(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY),
				((EventRepeatWeeklyEntity) entity.repeatPattern).daysOfWeek);
	}

	@Test
	public void testInvalidInterval() {
		var repeat = EventRepeatDTOUtil.EventRepeatWeeklyDTOUtil.of(builderFactory, (short) 0, "Europe/Vienna",
				LocalDate.parse("2020-01-15"), List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
		var newEvent = builderFactory.builder(EventNew.DataBuilder.class)
				.title("New event weekly with end")
				.description("New event description weekly with end")
				.start(ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T18:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(repeat)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();

		assertThrows(InvalidContentException.class,
				() -> handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent));
	}

	@Test
	public void testInvalidWeek() {
		var repeat = EventRepeatDTOUtil.EventRepeatWeeklyDTOUtil.of(builderFactory, (short) 1, "Europe/Vienna",
				LocalDate.parse("2020-01-15"), List.of());
		var newEvent = builderFactory.builder(EventNew.DataBuilder.class)
				.title("New event weekly with end")
				.description("New event description weekly with end")
				.start(ZonedDateTime.parse("2020-01-01T16:00:00+01:00[Europe/Vienna]"))
				.end(ZonedDateTime.parse("2020-01-01T18:00:00+01:00[Europe/Vienna]"))
				.fullday(false)
				.repeat(repeat)
				.tags(List.of())
				.referencedCalendars(List.of())
				.build();
		assertThrows(InvalidContentException.class,
				() -> handler.create(builderFactory, ownerlessCalendarKey.toString(), newEvent));
	}
}
