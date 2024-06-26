package at.bestsolution.quti.handler.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.Utils.ResultType;
import at.bestsolution.quti.model.modification.EventModificationCanceledEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class CancelHandlerTest extends EventHandlerTest<CancelHandler> {

	@Inject
	public CancelHandlerTest(CancelHandler handler) {
		super(handler);
	}

	@Test
	public void testSingleCancel() {
		var result = handler.cancel(basicCalendarKey, simpleEventKey, null);
		assertTrue(result.isOk());
		var modifications = modifications(simpleEventKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationCanceledEntity)
			.filter(m -> m.date.equals(LocalDate.EPOCH))
			.count());
	}

	@Test
	public void testSeriesCancel() {
		var result = handler.cancel(basicCalendarKey, repeatingDailyEndlessKey, LocalDate.parse("2024-01-01"));
		assertTrue(result.isOk());
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationCanceledEntity)
			.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
			.count());
	}

	@Test
	public void testSeriesMultiCancel() {
		var result = handler.cancel(basicCalendarKey, repeatingDailyEndlessKey, LocalDate.parse("2024-01-02"));
		assertTrue(result.isOk());
		result = handler.cancel(basicCalendarKey, repeatingDailyEndlessKey, LocalDate.parse("2024-01-02"));
		assertTrue(result.isOk());
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationCanceledEntity)
			.filter(m -> m.date.equals(LocalDate.parse("2024-01-02")))
			.count());
	}

	@Test
	public void testInvalidDate() {
		var result = handler.cancel(basicCalendarKey, repeatingDailyEndlessKey, LocalDate.parse("2023-12-31"));
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}
}
