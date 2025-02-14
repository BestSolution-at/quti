package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.jpa.model.modification.EventModificationGenericEntity;
import at.bestsolution.quti.service.InvalidArgumentException;
import at.bestsolution.quti.service.NotFoundException;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SetDescriptionHandlerTest extends EventHandlerTest<SetDescriptionHandlerJPA> {

	public SetDescriptionHandlerTest(SetDescriptionHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.setDescription(builderFactory, "abcd", simpleEventKey.toString(), "Abcd"));
	}

	@Test
	public void invalidEventKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.setDescription(builderFactory, basicCalendarKey.toString(), "abcd", "Acbd"));
	}

	@Test
	public void setDescriptionSingle() {
		handler.setDescription(
				builderFactory,
				basicCalendarKey.toString(),
				simpleEventKey.toString(),
				"A custom description");

		var event = event(simpleEventKey);
		assertEquals("A custom description", event.description);
	}

	@Test
	public void setDescriptionRepeat() {
		handler.setDescription(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-01",
				"A custom description");

		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationGenericEntity)
				.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
				.count());

		var mod = modifications.stream()
				.filter(m -> m instanceof EventModificationGenericEntity)
				.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
				.map(m -> (EventModificationGenericEntity) m)
				.findFirst()
				.orElseThrow();
		assertEquals("A custom description", mod.description);
	}

	@Test
	public void setDescriptionRepeatMulti() {
		handler.setDescription(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-01",
				"A custom description");

		handler.setDescription(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-01",
				"Another custom description");

		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationGenericEntity)
				.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
				.count());
		var mod = modifications.stream()
				.filter(m -> m instanceof EventModificationGenericEntity)
				.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
				.map(m -> (EventModificationGenericEntity) m)
				.findFirst()
				.orElseThrow();
		assertEquals("Another custom description", mod.description);
	}

	@Test
	public void invalidDate() {
		assertThrows(NotFoundException.class, () -> handler.setDescription(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2023-12-31",
				"A custom description"));
	}
}
