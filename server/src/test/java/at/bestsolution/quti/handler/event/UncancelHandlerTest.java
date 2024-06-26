package at.bestsolution.quti.handler.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.model.modification.EventModificationCanceledEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class UncancelHandlerTest extends EventHandlerTest<UncancelHandler> {

	@Inject
	public UncancelHandlerTest(UncancelHandler handler) {
		super(handler);
	}

	@Test
	public void uncancelSingle() {
		var modifications = modifications(simpleEventCanceledKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationCanceledEntity)
			.count());

		var result = handler.uncancel(basicCalendarKey, simpleEventCanceledKey, null);
		assertTrue(result.isOk());
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
			.map( m -> (EventModificationCanceledEntity)m)
			.filter( m -> m.date.equals(date))
			.count());

		var result = handler.uncancel(basicCalendarKey, repeatingDailyEndlessKey, LocalDate.parse("2024-05-09"));
		assertTrue(result.isOk());
		modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(0, modifications.stream()
			.filter(m -> m instanceof EventModificationCanceledEntity)
			.map( m -> (EventModificationCanceledEntity)m)
			.filter( m -> m.date.equals(date))
			.count());
	}
}
