// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.service.model;

import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.List;
import java.util.Optional;

import at.bestsolution.quti.calendar.service.model.mixins.EventDataMixin;

public interface Event {

	public interface Data extends _Base.BaseData, Event, EventDataMixin {
		public String key();

		public String title();

		public String description();

		public ZonedDateTime start();

		public ZonedDateTime end();

		public boolean fullday();

		public EventRepeat.Data repeat();

		public List<String> tags();

		public List<String> referencedCalendars();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<Event.Data> {
		public DataBuilder key(String key);

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

	public interface Patch extends _Base.BaseData, Event {
		public Optional<String> title();

		public _Base.Nillable<String> description();

		public Optional<ZonedDateTime> start();

		public Optional<ZonedDateTime> end();

		public _Base.Nillable<Boolean> fullday();

		public _Base.Nillable<EventRepeat.Data> repeat();

		public Optional<List<String>> tags();

		public Optional<List<String>> referencedCalendars();

	}

	public interface PatchBuilder extends _Base.BaseDataBuilder<Event.Patch> {
		public PatchBuilder title(String title);

		public PatchBuilder description(String description);

		public PatchBuilder start(ZonedDateTime start);

		public PatchBuilder end(ZonedDateTime end);

		public PatchBuilder fullday(Boolean fullday);

		public PatchBuilder repeat(EventRepeat.Data repeat);

		public <T extends EventRepeat.DataBuilder> PatchBuilder withRepeat(Class<T> clazz, Function<T, EventRepeat.Data> block);

		public PatchBuilder tags(List<String> tags);

		public PatchBuilder referencedCalendars(List<String> referencedCalendars);

	}
}
