// Generated by RSD - Do not modify
package at.bestsolution.quti.service.model.mixins;

import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.service.model.EventRepeat;

public interface EventDataMixin {
	public String title();

	public String description();

	public ZonedDateTime start();

	public ZonedDateTime end();

	public boolean fullday();

	public EventRepeat.Data repeat();

	public List<String> tags();

	public List<String> referencedCalendars();

}
