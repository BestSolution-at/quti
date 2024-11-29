package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.model.modification.EventModificationCanceledEntity;
import at.bestsolution.quti.service.Result.ResultType;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class CancelHandlerTest extends EventHandlerTest<CancelHandlerJPA> {

	@Inject
	public CancelHandlerTest(CancelHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		var result = handler.cancel(builderFactory, "abcd", simpleEventKey.toString());
		assertFalse(result.isOk());
	}

	@Test
	public void invalidEventKey() {
		var result = handler.cancel(builderFactory, basicCalendarKey.toString(), "abcd");
		assertFalse(result.isOk());
	}

	@Test
	public void testSingleCancel() {
		var result = handler.cancel(
			builderFactory,
			basicCalendarKey.toString(),
			simpleEventKey.toString());
		assertTrue(result.isOk());
		var modifications = modifications(simpleEventKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationCanceledEntity)
			.filter(m -> m.date.equals(LocalDate.EPOCH))
			.count());
	}

	@Test
	public void testSeriesCancel() {
		var result = handler.cancel(
			builderFactory,
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString() + "_2024-01-01");
		assertTrue(result.isOk());
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationCanceledEntity)
			.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
			.count());
	}

	@Test
	public void testSeriesMultiCancel() {
		var result = handler.cancel(
			builderFactory,
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString()+"_2024-01-02");
		assertTrue(result.isOk());

		result = handler.cancel(
			builderFactory,
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString()+"_2024-01-02");
		assertTrue(result.isOk());
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
			.filter(m -> m instanceof EventModificationCanceledEntity)
			.filter(m -> m.date.equals(LocalDate.parse("2024-01-02")))
			.count());
	}

	@Test
	public void testInvalidDate() {
		var result = handler.cancel(
			builderFactory,
			basicCalendarKey.toString(),
			repeatingDailyEndlessKey.toString()+"_2023-12-31");
		assertFalse(result.isOk());
		assertEquals(ResultType.NOT_FOUND, result.type());
	}
}
