package at.bestsolution.quti.calendar.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.calendar.service.InvalidArgumentException;
import at.bestsolution.quti.calendar.service.jpa.model.modification.EventModificationCanceledEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class UncancelHandlerTest extends EventHandlerTest<UncancelHandlerJPA> {

	@Inject
	public UncancelHandlerTest(UncancelHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.uncancel(builderFactory, "abcd", simpleEventKey.toString()));
	}

	@Test
	public void invalidEventKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.uncancel(builderFactory, basicCalendarKey.toString(), "abcd"));
	}

	@Test
	public void uncancelSingle() {
		var modifications = modifications(simpleEventCanceledKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationCanceledEntity)
				.count());

		handler.uncancel(builderFactory, basicCalendarKey.toString(), simpleEventCanceledKey.toString());
		modifications = modifications(simpleEventCanceledKey);
		assertEquals(0, modifications.stream()
				.filter(m -> m instanceof EventModificationCanceledEntity)
				.count());
	}

	@Test
	public void uncancelSeries() {
		var date = LocalDate.parse("2024-05-09");
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationCanceledEntity)
				.map(m -> (EventModificationCanceledEntity) m)
				.filter(m -> m.date.equals(date))
				.count());

		handler.uncancel(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-05-09");
		modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(0, modifications.stream()
				.filter(m -> m instanceof EventModificationCanceledEntity)
				.map(m -> (EventModificationCanceledEntity) m)
				.filter(m -> m.date.equals(date))
				.count());
	}
}
