package at.bestsolution.quti.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.service.jpa.model.modification.EventModificationCanceledEntity;
import at.bestsolution.quti.service.InvalidArgumentException;
import at.bestsolution.quti.service.NotFoundException;
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
		assertThrows(InvalidArgumentException.class,
				() -> handler.cancel(builderFactory, "abcd", simpleEventKey.toString()));
	}

	@Test
	public void invalidEventKey() {
		assertThrows(InvalidArgumentException.class,
				() -> handler.cancel(builderFactory, basicCalendarKey.toString(), "abcd"));
	}

	@Test
	public void testSingleCancel() {
		handler.cancel(
				builderFactory,
				basicCalendarKey.toString(),
				simpleEventKey.toString());
		var modifications = modifications(simpleEventKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationCanceledEntity)
				.filter(m -> m.date.equals(LocalDate.EPOCH))
				.count());
	}

	@Test
	public void testSeriesCancel() {
		handler.cancel(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-01");
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationCanceledEntity)
				.filter(m -> m.date.equals(LocalDate.parse("2024-01-01")))
				.count());
	}

	@Test
	public void testSeriesMultiCancel() {
		handler.cancel(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-02");

		handler.cancel(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-02");
		var modifications = modifications(repeatingDailyEndlessKey);
		assertEquals(1, modifications.stream()
				.filter(m -> m instanceof EventModificationCanceledEntity)
				.filter(m -> m.date.equals(LocalDate.parse("2024-01-02")))
				.count());
	}

	@Test
	public void testInvalidDate() {
		assertThrows(NotFoundException.class, () -> handler.cancel(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2023-12-31"));
	}
}
