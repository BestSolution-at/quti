// Generated by RSD - Do not modify
package at.bestsolution.quti.client.model;

import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.List;

import at.bestsolution.quti.client.model.mixins.EventDataMixin;

public interface EventNew {

	public interface Data extends _Base.BaseData, EventNew, EventDataMixin {
		public String title();

		public String description();

		public ZonedDateTime start();

		public ZonedDateTime end();

		public boolean fullday();

		public EventRepeat.Data repeat();

		public List<String> tags();

		public List<String> referencedCalendars();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<EventNew.Data> {
		public DataBuilder title(String title);

		public DataBuilder description(String description);

		public DataBuilder start(ZonedDateTime start);

		public DataBuilder end(ZonedDateTime end);

		public DataBuilder fullday(boolean fullday);

		public DataBuilder repeat(EventRepeat.Data repeat);

		public <T extends EventRepeat.DataBuilder> DataBuilder withRepeat(Class<T> clazz, Function<T, EventRepeat.Data> block);

		public DataBuilder tags(List<String> tags);

		public DataBuilder referencedCalendars(List<String> referencedCalendars);

	}
}