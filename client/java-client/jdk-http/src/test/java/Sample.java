import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.client.CalendarService;
import at.bestsolution.quti.client.EventService;
import at.bestsolution.quti.client.dto.CalendarNewDTO;
import at.bestsolution.quti.client.dto.EventRepeatWeeklyDTO;
import at.bestsolution.quti.client.jdkhttp.JDKQutiClient;

public class Sample {
	public static void main(String[] args) {
		var client = JDKQutiClient.create(URI.create("http://localhost:8080/api/calendar"));

		var b = client.builder(CalendarNewDTO.Builder.class);

		var calendarService = client.service(CalendarService.class);
		var calendar = calendarService.get("bf5a9b84-33ed-4ff8-9447-0a9e5cf5c24c");
		var result = calendarService.eventView("bf5a9b84-33ed-4ff8-9447-0a9e5cf5c24c", LocalDate.parse("2024-09-01"),
				LocalDate.parse("2024-09-30"), ZoneId.systemDefault());

		System.err.println(result);

		// var eventService = client.service(EventService.class);
		/*
		 * var key = calendarService.create( builder -> {
		 * return builder
		 * .name("Demo")
		 * .owner("quti@quti.dev")
		 * .build();
		 * });
		 * System.err.println(key);
		 */

		/*
		 * var event = eventService.create("bfb924f7-30d4-4c3b-85b3-868578f92fbf",
		 * builder -> {
		 * return builder
		 * .title("Sample Event")
		 * .description("A sample event")
		 * .start(ZonedDateTime.parse("2024-03-01T10:00:00+01:00[Europe/Paris]"))
		 * .end(ZonedDateTime.parse("2024-03-01T14:00:00+01:00[Europe/Paris]"))
		 * .withRepeat(EventRepeatWeeklyDTO.Builder.class, rBuilder -> {
		 * return rBuilder
		 * .interval((short)1)
		 * .timeZone(ZoneId.of("Europe/Paris"))
		 * .daysOfWeek(List.of(DayOfWeek.FRIDAY))
		 * .build();
		 * })
		 * .build();
		 * });
		 */

		/*
		 * eventService.move(
		 * "bfb924f7-30d4-4c3b-85b3-868578f92fbf",
		 * "d5f45696-3dd2-4f90-af53-8f926a62fb62_2024-03-08",
		 * ZonedDateTime.parse("2024-03-08T08:00:00+01:00[Europe/Paris]"),
		 * ZonedDateTime.parse("2024-03-08T12:00:00+01:00[Europe/Paris]")
		 * );
		 */

		/*
		 * var events = calendarService.eventView(
		 * "bfb924f7-30d4-4c3b-85b3-868578f92fbf",
		 * LocalDate.of(2024, 2, 1),
		 * LocalDate.of(2024, 4, 29),
		 * ZoneId.systemDefault()
		 * );
		 * events.stream()
		 * .map( e -> String.format("%s %s - %s: %s", e.start().toLocalDate(),
		 * e.start().toLocalTime(), e.end().toLocalTime(), e.title()))
		 * .forEach(System.err::println);
		 */
	}
}
