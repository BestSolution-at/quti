// package at.bestsolution.quti.service.jpa.event;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.time.LocalDate;
// import java.time.ZoneId;
// import java.time.ZoneOffset;
// import java.time.ZonedDateTime;

// import org.junit.jupiter.api.Test;

// import at.bestsolution.quti.rest.dto.EventRepeatDailyDTOImpl;
// import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatDailyEntity;
// import io.quarkus.test.junit.QuarkusTest;
// import jakarta.inject.Inject;

// @QuarkusTest
// public class UpdateHandlerTest extends EventHandlerTest<UpdateHandlerJPA> {
// @Inject
// public UpdateHandlerTest(UpdateHandlerJPA handler) {
// super(handler);
// }

// /*
// * @Test
// * public void updateSimpleEventTitle() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(simpleEventKey.toString());
// * dto.setTitle("Patched Title");
// *
// * var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
// * assertTrue(rv.isOk());
// * assertEquals("Patched Title", event(simpleEventKey).title);
// * assertEquals("A simple none repeating event",
// * event(simpleEventKey).description);
// * }
// *
// * @Test
// * public void updateSimpleEventTitle_Empty() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(simpleEventKey.toString());
// * dto.setTitle("");
// *
// * var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
// * assertTrue(rv.isNotOk());
// * assertEquals("Simple Event", event(simpleEventKey).title);
// * assertEquals("A simple none repeating event",
// * event(simpleEventKey).description);
// * }
// *
// * @Test
// * public void updateSimpleEventDescription() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(simpleEventKey.toString());
// * dto.setDescription("Patched Description");
// *
// * var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
// * assertTrue(rv.isOk());
// * assertEquals("Simple Event", event(simpleEventKey).title);
// * assertEquals("Patched Description", event(simpleEventKey).description);
// * }
// *
// * @Test
// * public void updateSimpleEventStart() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(simpleEventKey.toString());
// *
// dto.setStart(ZonedDateTime.parse("2024-01-10T06:00:00+01:00[Europe/Vienna]"))
// * ;
// *
// * var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
// * assertTrue(rv.isOk());
// * var entity = event(simpleEventKey);
// * assertEquals(entity.start,
// * ZonedDateTime.parse("2024-01-10T06:00:00+01:00[Europe/Vienna]").
// * withZoneSameInstant(ZoneOffset.UTC));
// * }
// *
// * @Test
// * public void updateSimpleEventEnd() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(simpleEventKey.toString());
// *
// dto.setEnd(ZonedDateTime.parse("2024-01-10T14:00:00+01:00[Europe/Vienna]"));
// *
// * var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
// * assertTrue(rv.isOk());
// * var entity = event(simpleEventKey);
// * assertEquals(entity.end,
// * ZonedDateTime.parse("2024-01-10T14:00:00+01:00[Europe/Vienna]").
// * withZoneSameInstant(ZoneOffset.UTC));
// * }
// *
// * @Test
// * public void updateRepeatEventStart() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(repeatingDailyEndlessKey.toString());
// *
// dto.setStart(ZonedDateTime.parse("2024-01-01T12:00:00+01:00[Europe/Vienna]"))
// * ;
// *
// * var rv = handler.update(basicCalendarKey, repeatingDailyEndlessKey, dto);
// * assertTrue(rv.isOk());
// *
// * var entity = event(repeatingDailyEndlessKey);
// * var modifications = modifications(repeatingDailyEndlessKey);
// * assertEquals(entity.start,
// * ZonedDateTime.parse("2024-01-01T12:00:00+01:00[Europe/Vienna]").
// * withZoneSameInstant(ZoneOffset.UTC));
// * assertEquals(3, modifications.size());
// * }
// *
// * @Test
// * public void updateSetNewRepeat() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(simpleEventKey.toString());
// * var repeat = new EventRepeatDailyDTOImpl();
// * repeat.interval = 7;
// * repeat.timeZone = ZoneId.of("Europe/Vienna");
// * dto.setRepeat(repeat);
// *
// * var rv = handler.update(basicCalendarKey, simpleEventKey, dto);
// * assertTrue(rv.isOk());
// * var entity = event(simpleEventKey);
// * assertNotNull(entity.repeatPattern);
// * assertTrue(entity.repeatPattern instanceof EventRepeatDailyEntity);
// * assertEquals(7, entity.repeatPattern.interval);
// * }
// *
// * @Test
// * public void updateRemoveRepeat() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(repeatingDailyEndlessKey.toString());
// * dto.setRepeat(null);
// * var rv = handler.update(basicCalendarKey, repeatingDailyEndlessKey, dto);
// * assertTrue(rv.isOk());
// * assertNull(event(repeatingDailyEndlessKey).repeatPattern);
// * assertEquals(0, modifications(repeatingDailyEndlessKey).size());
// * }
// *
// * @Test
// * public void updateEndRepeat() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(repeatingDailyEndlessKey.toString());
// * var repeat = new EventRepeatDailyPatchDTOImpl();
// * repeat.setEndDate(LocalDate.parse("2025-01-01"));
// * dto.setRepeat(repeat);
// *
// * var rv = handler.update(basicCalendarKey, repeatingDailyEndlessKey, dto);
// * assertTrue(rv.isOk());
// * var entity = event(repeatingDailyEndlessKey);
// * assertNotNull(entity.repeatPattern);
// * assertNotNull(entity.repeatPattern.endDate);
// * assertEquals(ZonedDateTime.parse("2025-01-01T22:59:59Z"),
// * entity.repeatPattern.endDate.withZoneSameInstant(ZoneOffset.UTC));
// * assertEquals(3, modifications(repeatingDailyEndlessKey).size());
// * }
// *
// * @Test
// * public void updateInterval() {
// * var dto = new EventPatchDTOImpl();
// * dto.setKey(repeatingDailyEndlessKey.toString());
// * var repeat = new EventRepeatDailyPatchDTOImpl();
// * repeat.setInterval((short) 2);
// * dto.setRepeat(repeat);
// *
// * var rv = handler.update(basicCalendarKey, repeatingDailyEndlessKey, dto);
// * assertTrue(rv.isOk());
// * var entity = event(repeatingDailyEndlessKey);
// * assertNotNull(entity.repeatPattern);
// * assertEquals(2, entity.repeatPattern.interval);
// * }
// */
// }
