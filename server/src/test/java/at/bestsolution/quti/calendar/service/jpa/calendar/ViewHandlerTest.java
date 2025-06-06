package at.bestsolution.quti.calendar.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.calendar.service.model.mixins.EventViewDataMixin.Status;
import at.bestsolution.quti.calendar.service.jpa.event.SetDescriptionHandlerJPA;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ViewHandlerTest extends CalendarHandlerTest<ViewHandlerJPA> {
	private SetDescriptionHandlerJPA descriptionHandler;

	@Inject
	public ViewHandlerTest(ViewHandlerJPA handler, SetDescriptionHandlerJPA descriptionHandler) {
		super(handler);
		this.descriptionHandler = descriptionHandler;
	}

	@Test
	public void testInvalidParams() {
		assertThrows(NullPointerException.class, () -> handler.eventView(
				builderFactory,
				null,
				null,
				null,
				null,
				null));
		assertThrows(NullPointerException.class, () -> handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				null,
				null,
				null,
				null));
		assertThrows(NullPointerException.class, () -> handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				null,
				null,
				null));
		assertThrows(NullPointerException.class, () -> handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-03-01"),
				null,
				null));

		assertThrows(IllegalArgumentException.class, () -> handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-03"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna")));
	}

	@Test
	public void testOneDay() {
		var result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.size());
	}

	@Test
	public void testViewDaily() {
		var result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-03-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		// 1 Simple
		// 31 Repeating Jan
		// 29 Repeating Feb
		// 1 Repeating March
		assertEquals(62, result.size());
		assertEquals(LocalDate.parse("2024-01-01"), result.get(0).start().toLocalDate());
		assertEquals(LocalDate.parse("2024-01-31"), result.get(31).start().toLocalDate());

		result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2025-02-01"),
				LocalDate.parse("2025-03-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(29, result.size());
		assertEquals(LocalDate.parse("2025-02-01"), result.get(0).start().toLocalDate());
		assertEquals(LocalDate.parse("2025-03-01"), result.get(28).start().toLocalDate());
	}

	@Test
	public void testViewDailyRef() {
		var result = handler.eventView(
				builderFactory,
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-31"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		// 31 Repeating + 1 Simple
		assertEquals(32, result.size());
		assertEquals(LocalDate.parse("2024-01-01"), result.get(0).start().toLocalDate());
		assertEquals(LocalDate.parse("2024-01-31"), result.get(31).start().toLocalDate());
	}

	@Test
	void testViewDailyAboveDaylightSaving() {
		var result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-03-25"),
				LocalDate.parse("2024-04-05"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		// 6 in March + 5 April
		assertEquals(12, result.size());
	}

	@Test
	public void testViewDailyCustResultTimezone() {
		var result = handler.eventView(
				builderFactory,
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-01-10"),
				LocalDate.parse("2024-01-10"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Z"));
		// 31 Repeating + 1 Simple
		assertEquals(2, result.size());
		assertEquals(ZonedDateTime.parse("2024-01-10T06:00Z"), result.get(0).start());
		assertEquals(ZonedDateTime.parse("2024-01-10T12:00Z"), result.get(1).start());
	}

	@Test
	public void testDaylightSavingCustResultTimezone() {
		var result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-03-25"),
				LocalDate.parse("2024-04-05"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Z"));
		assertEquals(12, result.size());
		assertEquals(ZonedDateTime.parse("2024-03-25T12:00Z"), result.get(0).start());
		assertEquals(ZonedDateTime.parse("2024-04-05T11:00Z"), result.get(11).start());
	}

	@Test
	public void testMoveEvents() {
		var result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-05-04"),
				LocalDate.parse("2024-05-06"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(3, result.size());

		result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-05-07"),
				LocalDate.parse("2024-05-09"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(2, result.size());
	}

	@Test
	public void testMoveRefEvents() {
		var result = handler.eventView(
				builderFactory,
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-05-04"),
				LocalDate.parse("2024-05-06"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(3, result.size());

		result = handler.eventView(
				builderFactory,
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-05-07"),
				LocalDate.parse("2024-05-09"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(2, result.size());
	}

	@Test
	public void testFulldaySimpleEvents() {
		var result = handler.eventView(
				builderFactory,
				fulldayCalendarKey.toString(),
				LocalDate.parse("2024-01-02"),
				LocalDate.parse("2024-01-09"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.size());

		result = handler.eventView(
				builderFactory,
				fulldayCalendarKey.toString(),
				LocalDate.parse("2023-12-31"),
				LocalDate.parse("2024-01-02"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.size());

		result = handler.eventView(
				builderFactory,
				fulldayCalendarKey.toString(),
				LocalDate.parse("2023-12-31"),
				LocalDate.parse("2024-01-21"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.size());

		result = handler.eventView(
				builderFactory,
				fulldayCalendarKey.toString(),
				LocalDate.parse("2023-01-05"),
				LocalDate.parse("2024-01-21"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.size());
	}

	@Test
	public void testFulldaySimpleEventsSingleDay() {
		var result = handler.eventView(
				builderFactory,
				fulldayCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.size());

		result = handler.eventView(
				builderFactory,
				fulldayCalendarKey.toString(),
				LocalDate.parse("2024-01-20"),
				LocalDate.parse("2024-01-20"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.size());

		result = handler.eventView(
				builderFactory,
				fulldayCalendarKey.toString(),
				LocalDate.parse("2023-12-31"),
				LocalDate.parse("2023-12-31"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(0, result.size());

		result = handler.eventView(
				builderFactory,
				fulldayCalendarKey.toString(),
				LocalDate.parse("2024-01-21"),
				LocalDate.parse("2024-01-21"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(0, result.size());
	}

	@Test
	public void testCancelSingle() {
		var result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2020-01-10"),
				LocalDate.parse("2020-01-10"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.size());
		assertEquals(Status.CANCELED, result.get(0).status());
	}

	@Test
	public void testCustomDescription() {
		descriptionHandler.description(
				builderFactory,
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString() + "_2024-01-01",
				"A custom description");

		var result = handler.eventView(
				builderFactory,
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));

		assertEquals(1, result.size());
		assertEquals(result.get(0).description(), "A custom description");

		var refResult = handler.eventView(
				builderFactory,
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Z"));

		assertEquals(1, refResult.size());
		assertEquals(refResult.get(0).description(), "A custom description");

	}
}
