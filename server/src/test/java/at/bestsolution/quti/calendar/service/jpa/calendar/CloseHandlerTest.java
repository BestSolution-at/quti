package at.bestsolution.quti.calendar.service.jpa.calendar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.calendar.service.jpa.model.EventEntity;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CloseHandlerTest extends CalendarHandlerTest<CloseHandlerJPA> {

	public CloseHandlerTest(CloseHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void testCloseSimpleEventPast() {
		handler.close(
				builderFactory,
				basicCalendarKey.toString(),
				ZonedDateTime.parse("2024-01-11T00:00:00+01:00[Europe/Vienna]"));
		var q = session.createQuery("""
				FROM
					Event e
				WHERE
					e.key = :key
				""", EventEntity.class)
				.setParameter("key", simpleEventKey);
		assertFalse(q.getResultList().isEmpty());
	}

	@Test
	public void testCloseSimpleEventFuture() {
		handler.close(
				builderFactory,
				basicCalendarKey.toString(),
				ZonedDateTime.parse("2024-01-10T00:00:00+01:00[Europe/Vienna]"));
		var q = session.createQuery("""
				FROM
					Event e
				WHERE
					e.key = :key
				""", EventEntity.class)
				.setParameter("key", simpleEventKey);
		assertTrue(q.getResultList().isEmpty());
	}

	@Test
	public void testCloseRecurringEventPast() {
		var endDate = ZonedDateTime.parse("2024-01-04T00:00:00+01:00[Europe/Vienna]");
		handler.close(
				builderFactory,
				basicCalendarKey.toString(),
				endDate);
		var q = session.createQuery("""
				FROM
					Event e
				WHERE
					e.key = :key
				""", EventEntity.class)
				.setParameter("key", repeatingDailyEndlessKey);
		var event = q.getSingleResult();
		assertNotNull(event);
		assertNotNull(event.repeatPattern);
		assertNotNull(event.repeatPattern.endDate);
		assertTrue(event.repeatPattern.endDate.isEqual(endDate));
	}
}
