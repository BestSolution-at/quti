// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.model;

import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.model.Event;
import at.bestsolution.quti.client.model.EventRepeat;
import at.bestsolution.quti.client.model.EventRepeatAbsoluteMonthly;
import at.bestsolution.quti.client.model.EventRepeatAbsoluteYearly;
import at.bestsolution.quti.client.model.EventRepeatDaily;
import at.bestsolution.quti.client.model.EventRepeatRelativeMonthly;
import at.bestsolution.quti.client.model.EventRepeatRelativeYearly;
import at.bestsolution.quti.client.model.EventRepeatWeekly;

public class EventDataImpl extends _BaseDataImpl implements Event.Data {
	EventDataImpl(JsonObject data) {
		super(data);
	}

	public String key() {
		return _JsonUtils.mapString(data, "key");
	}

	public String title() {
		return _JsonUtils.mapString(data, "title");
	}

	public String description() {
		return _JsonUtils.mapString(data, "description", null);
	}

	public ZonedDateTime start() {
		return _JsonUtils.mapZonedDateTime(data, "start");
	}

	public ZonedDateTime end() {
		return _JsonUtils.mapZonedDateTime(data, "end");
	}

	public boolean fullday() {
		return _JsonUtils.mapBoolean(data, "fullday");
	}

	public EventRepeat.Data repeat() {
		return _JsonUtils.mapObject(data, "repeat", EventRepeatDataImpl::of, null);
	}

	public List<String> tags() {
		return _JsonUtils.mapStrings(data, "tags");
	}

	public List<String> referencedCalendars() {
		return _JsonUtils.mapStrings(data, "referencedCalendars");
	}

	public static Event.Data of(JsonObject obj) {
		return new EventDataImpl(obj);
	}

	public String toString() {
		return "%s[%s=%s]".formatted(getClass().getSimpleName(), "key", key());
	}

	public static class DataBuilderImpl implements Event.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public Event.DataBuilder key(String key) {
			$builder.add("key", key);
			return this;
		}

		public Event.DataBuilder title(String title) {
			if (title == null) {
				return this;
			}
			$builder.add("title", title);
			return this;
		}

		public Event.DataBuilder description(String description) {
			if (description == null) {
				return this;
			}
			$builder.add("description", description);
			return this;
		}

		public Event.DataBuilder start(ZonedDateTime start) {
			if (start == null) {
				return this;
			}
			$builder.add("start", start.toString());
			return this;
		}

		public Event.DataBuilder end(ZonedDateTime end) {
			if (end == null) {
				return this;
			}
			$builder.add("end", end.toString());
			return this;
		}

		public Event.DataBuilder fullday(boolean fullday) {
			$builder.add("fullday", fullday);
			return this;
		}

		public Event.DataBuilder repeat(EventRepeat.Data repeat) {
			if (repeat == null) {
				return this;
			}
			$builder.add("repeat", ((_BaseDataImpl)repeat).data);
			return this;
		}


		public <T extends EventRepeat.DataBuilder> DataBuilder withRepeat(Class<T> clazz, Function<T, EventRepeat.Data> block) {
			EventRepeat.DataBuilder b;
			if (clazz == EventRepeatDaily.DataBuilder.class) {
				b = EventRepeatDailyDataImpl.builder();
			} else if (clazz == EventRepeatWeekly.DataBuilder.class) {
				b = EventRepeatWeeklyDataImpl.builder();
			} else if (clazz == EventRepeatAbsoluteMonthly.DataBuilder.class) {
				b = EventRepeatAbsoluteMonthlyDataImpl.builder();
			} else if (clazz == EventRepeatAbsoluteYearly.DataBuilder.class) {
				b = EventRepeatAbsoluteYearlyDataImpl.builder();
			} else if (clazz == EventRepeatRelativeMonthly.DataBuilder.class) {
				b = EventRepeatRelativeMonthlyDataImpl.builder();
			} else if (clazz == EventRepeatRelativeYearly.DataBuilder.class) {
				b = EventRepeatRelativeYearlyDataImpl.builder();
			} else {
				throw new IllegalArgumentException();
			}
			return repeat(block.apply(clazz.cast(b)));
		}

		public Event.DataBuilder tags(List<String> tags) {
			if (tags == null) {
				return this;
			}
			$builder.add("tags", _JsonUtils.toJsonStringArray(tags));
			return this;
		}

		public Event.DataBuilder referencedCalendars(List<String> referencedCalendars) {
			if (referencedCalendars == null) {
				return this;
			}
			$builder.add("referencedCalendars", _JsonUtils.toJsonStringArray(referencedCalendars));
			return this;
		}

		public Event.Data build() {
			return new EventDataImpl($builder.build());
		}
	}

	public static Event.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}
