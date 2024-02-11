package at.bestsolution.qutime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.json.Json;

@QuarkusTest
public class CalendarResourceTest extends BaseTest {
	@Test
	void testCreate() {
		given()
				.body("demo")
				.post("/api/calendar")
				.then()
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
				.body("key", is(basicCalendarKey))
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
	void testUpdateName() {
		var patch = Json.createPatchBuilder()
				.replace("name", "My Updated Calendar")
				.build().toString();
		given()
				.body(patch)
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
	void testAddOwner() {
		var patch = Json.createPatchBuilder()
				.add("owner", "testowner@bestsolution.at")
				.build()
				.toString();
		given()
				.body(patch)
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
	void testAddUnknownProperty() {
		var patch = Json.createPatchBuilder()
				.add("unknown", "Hello World")
				.build()
				.toString();
		given()
				.body(patch)
				.patch(String.format("/api/calendar/%s", ownerlessCalendarKey))
				.then()
				.statusCode(422);
	}

	@Test
	void testAddOwnerExisting() {
		var patch = Json.createPatchBuilder()
				.add("owner", "newowner@bestsolution.at")
				.build()
				.toString();
		given()
				.body(patch)
				.patch(String.format("/api/calendar/%s", basicCalendarKey))
				.then()
				.statusCode(422);
	}

	@Test
	void testReplaceOwnerExisting() {
		var patch = Json.createPatchBuilder()
				.replace("owner", "newowner@bestsolution.at")
				.build()
				.toString();
		given()
				.body(patch)
				.patch(String.format("/api/calendar/%s", basicCalendarKey))
				.then()
				.statusCode(422);
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
				.body("[0].key", is(repeatingDailyEndlessKey.toString()+"_2024-01-01"))
				.body("[0].@type", is("series"));

	}

	@Test
	void testViewDailyAbsoveDaylight() {
		given()
				.queryParam("from", LocalDate.parse("2024-03-25").toString())
				.queryParam("to", LocalDate.parse("2024-04-05").toString())
				.queryParam("timezone", "Europe/Vienna")
				.get(String.format("/api/calendar/%s/view", basicCalendarKey))
				.then()
				.statusCode(200)
				.body("[0].start", is("2024-03-25T13:00:00+01:00[Europe/Vienna]"))
				.body("[11].key", is(repeatingDailyEndlessKey.toString()+"_2024-04-05"))
				.body("[11].@type", is("series"))
				.body("[11].start", is("2024-04-05T13:00:00+02:00[Europe/Vienna]"));
	}
}
