// Generated by RSD - Do not modify
package at.bestsolution.quti.client.model;

import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.client.model.mixins.EventViewDataMixin;

public interface SingleEventView {

	public interface Data extends _Base.BaseData, SingleEventView, EventViewDataMixin, EventView.Data {
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

	public interface DataBuilder extends _Base.BaseDataBuilder<SingleEventView.Data>, EventView.DataBuilder {
		public DataBuilder key(String key);

		public DataBuilder calendarKey(String calendarKey);

		public DataBuilder title(String title);

		public DataBuilder description(String description);

		public DataBuilder owner(String owner);

		public DataBuilder status(EventViewDataMixin.Status status);

		public DataBuilder start(ZonedDateTime start);

		public DataBuilder end(ZonedDateTime end);

		public DataBuilder tags(List<String> tags);

		public DataBuilder referencedCalendars(List<String> referencedCalendars);

	}
}