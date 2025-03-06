// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.model;

import java.util.function.Function;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.model.DateTimeRange;
import at.bestsolution.quti.client.model.EventSearch;

public class EventSearchDataImpl extends _BaseDataImpl implements EventSearch.Data {
	EventSearchDataImpl(JsonObject data) {
		super(data);
	}

	public List<String> tags() {
		return _JsonUtils.mapStrings(data, "tags");
	}

	public DateTimeRange.Data startRange() {
		return _JsonUtils.mapObject(data, "startRange", DateTimeRangeDataImpl::of, null);
	}

	public DateTimeRange.Data endRange() {
		return _JsonUtils.mapObject(data, "endRange", DateTimeRangeDataImpl::of, null);
	}

	public static EventSearch.Data of(JsonObject obj) {
		return new EventSearchDataImpl(obj);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public static class DataBuilderImpl implements EventSearch.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public EventSearch.DataBuilder tags(List<String> tags) {
			if (tags == null) {
				return this;
			}
			$builder.add("tags", _JsonUtils.toJsonStringArray(tags));
			return this;
		}

		public EventSearch.DataBuilder startRange(DateTimeRange.Data startRange) {
			if (startRange == null) {
				return this;
			}
			$builder.add("startRange", ((_BaseDataImpl)startRange).data);
			return this;
		}


		public <T extends DateTimeRange.DataBuilder> DataBuilder withStartRange(Class<T> clazz, Function<T, DateTimeRange.Data> block) {
			var b = DateTimeRangeDataImpl.builder();
			return startRange(block.apply(clazz.cast(b)));
		}

		public EventSearch.DataBuilder endRange(DateTimeRange.Data endRange) {
			if (endRange == null) {
				return this;
			}
			$builder.add("endRange", ((_BaseDataImpl)endRange).data);
			return this;
		}


		public <T extends DateTimeRange.DataBuilder> DataBuilder withEndRange(Class<T> clazz, Function<T, DateTimeRange.Data> block) {
			var b = DateTimeRangeDataImpl.builder();
			return endRange(block.apply(clazz.cast(b)));
		}

		public EventSearch.Data build() {
			return new EventSearchDataImpl($builder.build());
		}
	}

	public static EventSearch.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}
