package at.bestsolution.quti.handler.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.Utils.ResultType;
import at.bestsolution.quti.model.modification.EventModificationGenericEntity;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SetDescriptionHandlerTest extends EventHandlerTest<SetDescriptionHandler> {

	public SetDescriptionHandlerTest(SetDescriptionHandler handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		var result = handler.setDescription("abcd", simpleEventKey.toString(), "Abcd");
		assertFalse(result.isOk());
	}

	@Test
	public void invalidEventKey() {
		var result = handler.setDescription(basicCalendarKey.toString(), "abcd", "Acbd");
		assertFalse(result.isOk());
	}

	@Test
	public void setDescriptionSingle() {
		var result = handler.setDescription(
			basicCalendarKey.toString(),
			simpleEventKey.toString(),
			"A custom description");

			assertTrue(result.isOk());
			var event = event(simpleEventKey);
			assertEquals("A custom description", event.description);
	}

	@Test
	public void setDescriptionRepeat() {
		var result = handler.setDescription(
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString()+"_2024-01-01",
			"A custom description");

		assertTrue(result.isOk());
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
			.filter( m -> m instanceof EventModificationGenericEntity)
			.filter( m -> m.date.equals(LocalDate.parse("2024-01-01")))
			.count());

		var mod = modifications.stream()
			.filter( m -> m instanceof EventModificationGenericEntity)
			.filter( m -> m.date.equals(LocalDate.parse("2024-01-01")))
			.map( m -> (EventModificationGenericEntity)m)
			.findFirst()
			.orElseThrow();
		assertEquals("A custom description", mod.description);
	}

	@Test
	public void setDescriptionRepeatMulti() {
		var result = handler.setDescription(
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString()+"_2024-01-01",
			"A custom description");
		assertTrue(result.isOk());

		handler.setDescription(
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString()+"_2024-01-01",
			"Another custom description");
		assertTrue(result.isOk());

		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
			.filter( m -> m instanceof EventModificationGenericEntity)
			.filter( m -> m.date.equals(LocalDate.parse("2024-01-01")))
			.count());
		var mod = modifications.stream()
			.filter( m -> m instanceof EventModificationGenericEntity)
			.filter( m -> m.date.equals(LocalDate.parse("2024-01-01")))
			.map( m -> (EventModificationGenericEntity)m)
			.findFirst()
			.orElseThrow();
		assertEquals("Another custom description", mod.description);
	}

	@Test
	public void invalidDate() {
		var result = handler.setDescription(
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString()+"_2023-12-31",
			"A custom description");

		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}
}
