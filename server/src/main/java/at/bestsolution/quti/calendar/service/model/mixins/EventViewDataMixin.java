// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.service.model.mixins;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventViewDataMixin {
	public enum Status {
		ACCEPTED,
		CANCELED,
	}

	public String key();

	public String calendarKey();

	public String title();

	public String description();

	public String owner();

	public EventViewDataMixin.Status status();

	public ZonedDateTime start();

	public ZonedDateTime end();

	public List<String> tags();

	public List<String> referencedCalendars();

}
