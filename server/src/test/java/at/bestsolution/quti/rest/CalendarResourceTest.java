package at.bestsolution.quti.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import at.bestsolution.quti.BaseTest;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CalendarResourceTest extends BaseTest {
	@Test
	void testCreate() {
		given()
				.header("Content-Type", "application/json")
				.body("{ \"name\": \"demo\" }")
				.post("/api/calendar")
				.then()
				.body(notNullValue())
				.statusCode(201)
				.header("Location", notNullValue());
	}

	@Test
	void testGet() {
		given()
				.get(String.format("/api/calendar/%s", basicCalendarKey))
				.then()
				.statusCode(200)
				.body("owner", is("cutime@bestsolution.at"))
				.body("key", is(basicCalendarKey.toString()))
				.body("name", is("My Calendar"));
	}

	@Test
	void testGetInvalidUUID() {
		given()
				.get("/api/calendar/abcd")
				.then()
				.statusCode(400);
	}

	@Test
	void testGetUnkownUUID() {
		given()
				.get(String.format("/api/calendar/%s", UUID.randomUUID().toString()))
				.then()
				.statusCode(404);
	}

	@Test
	@Disabled
	void testUpdateName() {
		given()
				.body("""
						{
							"name": "My Updated Calendar"
						}
						""")
				.header("Content-Type", "application/json")
				.patch(String.format("/api/calendar/%s", basicCalendarKey))
				.then()
				.statusCode(204);

		given()
				.get(String.format("/api/calendar/%s", basicCalendarKey))
				.then()
				.statusCode(200)
				.body("name", is("My Updated Calendar"));
	}

	@Test
	@Disabled
	void testAddOwner() {
		given()
				.body("""
						{
							"owner": "testowner@bestsolution.at"
						}
						""")
				.header("Content-Type", "application/json")
				.patch(String.format("/api/calendar/%s", ownerlessCalendarKey))
				.then()
				.statusCode(204);
		given()
				.get("/api/calendar/" + ownerlessCalendarKey)
				.then()
				.statusCode(200)
				.body("owner", is("testowner@bestsolution.at"));
	}

	@Test
	@Disabled
	void testAddOwnerExisting() {
		given()
				.body("""
						{
							"owner": "newowner@bestsolution.at"
						}
						""")
				.header("Content-Type", "application/json")
				.patch(String.format("/api/calendar/%s", basicCalendarKey))
				.then()
				.statusCode(400);
	}

	@Test
	@Disabled
	void testReplaceOwnerExisting() {
		given()
				.body("""
						{
							"owner": "newowner@bestsolution.at"
						}
						""")
				.header("Content-Type", "application/json")
				.patch(String.format("/api/calendar/%s", basicCalendarKey))
				.then()
				.statusCode(400);
	}

	@Test
	void testViewDaily() {
		given()
				.queryParam("from", LocalDate.parse("2024-01-01").toString())
				.queryParam("to", LocalDate.parse("2024-01-31").toString())
				.queryParam("timezone", "Europe/Vienna")
				.get(String.format("/api/calendar/%s/view", basicCalendarKey))
				.then()
				.statusCode(200)
				.body("[0].key", is(repeatingDailyEndlessKey.toString() + "_2024-01-01"))
				.body("[0].@type", is("series"));

	}

	// 2024-03-25T13:00:00+01:00[Europe/Vienna]
	// 2024-03-25T13:00+01:00[Europe/Vienna]
	@Test
	void testViewDailyAbsoveDaylight() {
		given()
				.queryParam("from", LocalDate.parse("2024-03-25").toString())
				.queryParam("to", LocalDate.parse("2024-04-05").toString())
				.queryParam("timezone", "Europe/Vienna")
				.get(String.format("/api/calendar/%s/view", basicCalendarKey))
				.then()
				.statusCode(200)
				.body("[0].start", is("2024-03-25T13:00+01:00[Europe/Vienna]"))
				.body("[11].key", is(repeatingDailyEndlessKey.toString() + "_2024-04-05"))
				.body("[11].@type", is("series"))
				.body("[11].start", is("2024-04-05T13:00+02:00[Europe/Vienna]"));
	}
}
