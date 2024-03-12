package at.bestsolution.quti.client.dto;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventViewDTO extends BaseDTO {
	public enum Status {
		ACCEPTED,
		CANCELED
	}

    public String key();
	public String calendarKey();
	public String title();
	public String description();
	public String owner();
	public Status status();
	public ZonedDateTime start();
	public ZonedDateTime end();
	public List<String> tags();
	public List<String> referencedCalendars();

    public interface SingleEventViewDTO extends EventViewDTO {}
    public interface SeriesMovedEventViewDTO extends EventViewDTO {
        public String masterEventKey();
		public ZonedDateTime originalStart();
		public ZonedDateTime originalEnd();
    }

    public interface SeriesEventViewDTO extends EventViewDTO {
		public String masterEventKey();
	}
}
