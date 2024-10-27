package at.bestsolution.quti.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.dto.EventViewDTO.Status;
import at.bestsolution.quti.service.jpa.event.SetDescriptionHandlerJPA;
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
		assertThrows(NullPointerException.class, () -> handler.view(
				null,
				null,
				null,
				null,
				null));
		assertThrows(NullPointerException.class, () -> handler.view(
				basicCalendarKey.toString(),
				null,
				null,
				null,
				null));
		assertThrows(NullPointerException.class, () -> handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				null,
				null,
				null));
		assertThrows(NullPointerException.class, () -> handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-03-01"),
				null,
				null));

		assertThrows(IllegalArgumentException.class, () -> handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-03"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna")));
	}

	@Test
	public void testOneDay() {
		var result = handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.value().size());
	}

	@Test
	public void testViewDaily() {
		var result = handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-03-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		// 1 Simple
		// 31 Repeating Jan
		// 29 Repeating Feb
		// 1 Repeating March
		assertEquals(62, result.value().size());
		assertEquals(LocalDate.parse("2024-01-01"), result.value().get(0).start.toLocalDate());
		assertEquals(LocalDate.parse("2024-01-31"), result.value().get(31).start.toLocalDate());

		result = handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2025-02-01"),
				LocalDate.parse("2025-03-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(29, result.value().size());
		assertEquals(LocalDate.parse("2025-02-01"), result.value().get(0).start.toLocalDate());
		assertEquals(LocalDate.parse("2025-03-01"), result.value().get(28).start.toLocalDate());
	}

	@Test
	public void testViewDailyRef() {
		var result = handler.view(
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-31"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		// 31 Repeating + 1 Simple
		assertEquals(32, result.value().size());
		assertEquals(LocalDate.parse("2024-01-01"), result.value().get(0).start.toLocalDate());
		assertEquals(LocalDate.parse("2024-01-31"), result.value().get(31).start.toLocalDate());
	}

	@Test
	void testViewDailyAboveDaylightSaving() {
		var result = handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-03-25"),
				LocalDate.parse("2024-04-05"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		// 6 in March + 5 April
		assertEquals(12, result.value().size());
	}

	@Test
	public void testViewDailyCustResultTimezone() {
		var result = handler.view(
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-01-10"),
				LocalDate.parse("2024-01-10"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Z"));
		// 31 Repeating + 1 Simple
		assertEquals(2, result.value().size());
		assertEquals(ZonedDateTime.parse("2024-01-10T06:00Z"), result.value().get(0).start);
		assertEquals(ZonedDateTime.parse("2024-01-10T12:00Z"), result.value().get(1).start);
	}

	@Test
	public void testDaylightSavingCustResultTimezone() {
		var result = handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-03-25"),
				LocalDate.parse("2024-04-05"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Z"));
		assertEquals(12, result.value().size());
		assertEquals(ZonedDateTime.parse("2024-03-25T12:00Z"), result.value().get(0).start);
		assertEquals(ZonedDateTime.parse("2024-04-05T11:00Z"), result.value().get(11).start);
	}

	@Test
	public void testMoveEvents() {
		var result = handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-05-04"),
				LocalDate.parse("2024-05-06"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(3, result.value().size());

		result = handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-05-07"),
				LocalDate.parse("2024-05-09"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(2, result.value().size());
	}

	@Test
	public void testMoveRefEvents() {
		var result = handler.view(
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-05-04"),
				LocalDate.parse("2024-05-06"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(3, result.value().size());

		result = handler.view(
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-05-07"),
				LocalDate.parse("2024-05-09"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));
		assertEquals(2, result.value().size());
	}

	@Test
	public void testFulldaySimpleEvents() {
		var result = handler.view(
			fulldayCalendarKey.toString(),
			LocalDate.parse("2024-01-02"),
			LocalDate.parse("2024-01-09"),
			ZoneId.of("Europe/Vienna"),
			ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.value().size());
	}

	@Test
	public void testCancelSingle() {
		var result = handler.view(
			basicCalendarKey.toString(),
			LocalDate.parse("2020-01-10"),
			LocalDate.parse("2020-01-10"),
			ZoneId.of("Europe/Vienna"),
			ZoneId.of("Europe/Vienna"));
		assertEquals(1, result.value().size());
		assertEquals(Status.CANCELED, result.value().get(0).status);
	}

	@Test
	public void testCustomDescription() {
			descriptionHandler.setDescription(
				basicCalendarKey.toString(),
				repeatingDailyEndlessKey.toString()+"_2024-01-01",
				"A custom description");

			var result = handler.view(
				basicCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Europe/Vienna"));

			assertEquals(1, result.value().size());
			assertEquals(result.value().get(0).description, "A custom description");

			var refResult = handler.view(
				referenceCalendarKey.toString(),
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2024-01-01"),
				ZoneId.of("Europe/Vienna"),
				ZoneId.of("Z"));

			assertEquals(1, refResult.value().size());
			assertEquals(refResult.value().get(0).description, "A custom description");

	}
}
