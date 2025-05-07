package at.bestsolution.quti.calendar.service.jpa.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import at.bestsolution.quti.calendar.service.InvalidArgumentException;
import at.bestsolution.quti.calendar.service.model.DateTimeRange;
import at.bestsolution.quti.calendar.service.model.EventSearch;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SearchHandlerTest extends EventHandlerTest<SearchHandlerJPA> {

	public SearchHandlerTest(SearchHandlerJPA handler) {
		super(handler);
	}

	@Test
	public void invalidCalendarKey() {
		var search = builderFactory.builder(EventSearch.DataBuilder.class)
				.build();
		assertThrows(InvalidArgumentException.class,
				() -> handler.search(builderFactory, "abcd", search, null));
	}

	@Test
	public void testEmptyFilter() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testTagsExact() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.tags(List.of("sample-tag-1"))
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testTagsMore() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.tags(List.of("sample-tag-1", "unknown"))
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testTagsNoMatch() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.tags(List.of("unknown"))
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void testStartMinOnly() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.withStartRange(DateTimeRange.DataBuilder.class, b -> {
					return b
							.min(ZonedDateTime.parse("2024-06-10T01:00:00+01:00[Europe/Vienna]"))
							.build();
				})
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testStartMaxOnly() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.withStartRange(DateTimeRange.DataBuilder.class, b -> {
					return b
							.max(ZonedDateTime.parse("2020-02-01T06:00:00+01:00[Europe/Vienna]"))
							.build();
				})
				.build();
		var result = handler.search(
				builderFactory,
				basicCalendarKey.toString(),
				search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testStartRange() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.withStartRange(DateTimeRange.DataBuilder.class, b -> {
					return b
							.min(ZonedDateTime.parse("2024-06-10T01:00:00+01:00[Europe/Vienna]"))
							.max(ZonedDateTime.parse("2024-06-10T20:00:00+01:00[Europe/Vienna]"))
							.build();
				})
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testEndMinOnly() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.withEndRange(DateTimeRange.DataBuilder.class, b -> {
					return b
							.min(ZonedDateTime.parse("2024-06-10T01:00:00+01:00[Europe/Vienna]"))
							.build();
				})
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testEndMaxOnly() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.withEndRange(DateTimeRange.DataBuilder.class, b -> {
					return b
							.max(ZonedDateTime.parse("2020-02-01T06:00:00+01:00[Europe/Vienna]"))
							.build();
				})
				.build();
		var result = handler.search(
				builderFactory,
				basicCalendarKey.toString(),
				search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testEndRange() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.withEndRange(DateTimeRange.DataBuilder.class, b -> {
					return b
							.min(ZonedDateTime.parse("2024-06-10T01:00:00+01:00[Europe/Vienna]"))
							.max(ZonedDateTime.parse("2024-06-10T20:00:00+01:00[Europe/Vienna]"))
							.build();
				})
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testCombined() {
		var search = builderFactory
				.builder(EventSearch.DataBuilder.class)
				.tags(List.of("sample-tag-1"))
				.withStartRange(DateTimeRange.DataBuilder.class, b -> {
					return b
							.min(ZonedDateTime.parse("2024-01-10T01:00:00+01:00[Europe/Vienna]"))
							.max(ZonedDateTime.parse("2024-01-10T20:00:00+01:00[Europe/Vienna]"))
							.build();
				})
				.build();
		var result = handler.search(builderFactory, basicCalendarKey.toString(), search, null);
		assertNotNull(result);
		assertEquals(1, result.size());
	}
}
