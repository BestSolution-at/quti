// Generated by RSD - Do not modify
package at.bestsolution.quti.client.dto;

import java.time.ZonedDateTime;
import java.util.List;

public interface MixinEventDataDTO {
	public interface Builder {
	}

	/**
	 * basic description
	 */
	public String title();

	/**
	 * a more detailed description
	 */
	public String description();

	/**
	 * start time
	 */
	public ZonedDateTime start();

	/**
	 * end time
	 */
	public ZonedDateTime end();

	/**
	 * mark it as a fullday event
	 */
	public boolean fullday();

	/**
	 * the repeat pattern
	 */
	public EventRepeatDTO repeat();

	/**
	 * a list of custom tags
	 */
	public List<String> tags();

	/**
	 * other calendars this event is referenced in
	 */
	public List<String> referencedCalendars();
}
