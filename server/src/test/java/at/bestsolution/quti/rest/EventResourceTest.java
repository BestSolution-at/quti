package at.bestsolution.quti.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.BaseTest;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class EventResourceTest extends BaseTest {

	@Test
	void testGet() {
		given()
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, simpleEventKey))
				.then()
				.statusCode(200)
				.body("key", is(simpleEventKey.toString()))
				.body("title", is("Simple Event"))
				.body("description", is("A simple none repeating event"))
				.body("start", is("2024-01-10T06:00:00Z"))
				.body("end", is("2024-01-10T12:00:00Z"));

		given()
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, simpleSummerEventKey))
				.then()
				.statusCode(200)
				.body("key", is(simpleSummerEventKey.toString()))
				.body("title", is("Simple Summer Event"))
				.body("description", is("A simple none repeating event in summer"))
				.body("start", is("2024-06-10T05:00:00Z"))
				.body("end", is("2024-06-10T11:00:00Z"));
	}

	@Test
	void testInvalidEventKey() {
		given()
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, "abcd"))
				.then()
				.statusCode(400);
	}

	@Test
	void testWrongCalendarEventKey() {
		given()
				.get(String.format("/api/calendar/%s/events/%s", ownerlessCalendarKey, simpleEventKey))
				.then()
				.statusCode(404);
	}

	@Test
	void testUnknownEventKey() {
		given()
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, UUID.randomUUID()))
				.then()
				.statusCode(404);
	}

	@Test
	void testGetTimezoneEurope() {
		given()
				.header("timezone", "Europe/Vienna")
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, simpleEventKey))
				.then()
				.statusCode(200)
				.body("key", is(simpleEventKey.toString()))
				.body("start", is("2024-01-10T07:00:00+01:00[Europe/Vienna]"))
				.body("end", is("2024-01-10T13:00:00+01:00[Europe/Vienna]"));

		given()
				.header("timezone", "Europe/Vienna")
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, simpleSummerEventKey))
				.then()
				.statusCode(200)
				.body("key", is(simpleSummerEventKey.toString()))
				.body("start", is("2024-06-10T07:00:00+02:00[Europe/Vienna]"))
				.body("end", is("2024-06-10T13:00:00+02:00[Europe/Vienna]"));

		given()
				.header("timezone", "+01:00")
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, simpleEventKey))
				.then()
				.statusCode(200)
				.body("key", is(simpleEventKey.toString()))
				.body("start", is("2024-01-10T07:00:00+01:00"))
				.body("end", is("2024-01-10T13:00:00+01:00"));
	}

	@Test
	void testGetTimezoneUS() {
		given()
				.header("timezone", "America/New_York")
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, simpleEventKey))
				.then()
				.statusCode(200)
				.body("key", is(simpleEventKey.toString()))
				.body("start", is("2024-01-10T01:00:00-05:00[America/New_York]"))
				.body("end", is("2024-01-10T07:00:00-05:00[America/New_York]"));

		given()
				.header("timezone", "US/Pacific")
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, simpleEventKey))
				.then()
				.statusCode(200)
				.body("key", is(simpleEventKey.toString()))
				.body("start", is("2024-01-09T22:00:00-08:00[US/Pacific]"))
				.body("end", is("2024-01-10T04:00:00-08:00[US/Pacific]"));
	}

	@Test
	void testGetRepeating() {
		var data = given()
				.get(String.format("/api/calendar/%s/events/%s", basicCalendarKey, repeatingDailyEndlessKey))
				.then()
				.statusCode(200)
				.body("key", is(repeatingDailyEndlessKey.toString()))
				.body("repeat.@type", is("daily"))
				.body("repeat.interval", is(1))
				.body("repeat.timeZone", is("Europe/Vienna"))
				.body("repeat.endDate", nullValue())
				.extract()
				.asString();
	}

	@Test
	void testActionEndRepeating() {
		given()
		.header("Content-Type", "application/json")
		.body("\"2024-01-10\"")
			.put(String.format("/api/calendar/%s/events/%s/action/end-repeat", basicCalendarKey, repeatingDailyEndlessKey))
			.then()
			.statusCode(204)
		;
	}

	@Test
	void testActionDescription() {
		given()
		.header("Content-Type", "application/json")
		.body("A custom description")
			.put(String.format("/api/calendar/%s/events/%s/action/description", basicCalendarKey, repeatingDailyEndlessKey+"_2024-01-01"))
			.then()
			.statusCode(204)
		;
	}
}
