package at.bestsolution.quti.dto;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

@JsonbTypeInfo({
		@JsonbSubtype(alias = "single", type = EventViewDTO.SingleEventViewDTO.class),
		@JsonbSubtype(alias = "series-moved", type = EventViewDTO.SeriesMovedEventViewDTO.class),
		@JsonbSubtype(alias = "series", type = EventViewDTO.SeriesEventViewDTO.class)
})
public abstract class EventViewDTO {
	public static enum Status {
		ACCEPTED,
		CANCELED
	}

	public String key;
	public String calendarKey;
	public String owner;
	public String title;
	public String description;
	public ZonedDateTime start;
	public ZonedDateTime end;
	public List<String> tags;
	public List<String> referencedCalendars;
	public Status status = Status.ACCEPTED;

	public static class SingleEventViewDTO extends EventViewDTO {
	}

	public static class SeriesMovedEventViewDTO extends EventViewDTO {
		public String masterEventKey;
		public ZonedDateTime originalStart;
		public ZonedDateTime originalEnd;
	}

	public static class SeriesEventViewDTO extends EventViewDTO {
		public String masterEventKey;
	}
}
