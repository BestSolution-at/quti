// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.model;

import java.time.LocalDate;
import java.time.ZoneId;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.service.model.EventRepeatDaily;

public class EventRepeatDailyDataImpl extends _BaseDataImpl implements EventRepeatDaily.Data {
	EventRepeatDailyDataImpl(JsonObject data) {
		super(data);
	}

	public short interval() {
		return _JsonUtils.mapShort(data, "interval");
	}

	public LocalDate endDate() {
		return _JsonUtils.mapLocalDate(data, "endDate", null);
	}

	public ZoneId timeZone() {
		return _JsonUtils.mapLiteral(data, "timeZone", ZoneId::of);
	}

	public static EventRepeatDaily.Data of(JsonObject obj) {
		return new EventRepeatDailyDataImpl(obj);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public static class DataBuilderImpl implements EventRepeatDaily.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public DataBuilderImpl() {
			$builder.add("@type", "daily");
		}

		public EventRepeatDaily.DataBuilder interval(short interval) {
			$builder.add("interval", interval);
			return this;
		}

		public EventRepeatDaily.DataBuilder endDate(LocalDate endDate) {
			if (endDate == null) {
				return this;
			}
			$builder.add("endDate", endDate.toString());
			return this;
		}

		public EventRepeatDaily.DataBuilder timeZone(ZoneId timeZone) {
			if (timeZone == null) {
				return this;
			}
			$builder.add("timeZone", timeZone.toString());
			return this;
		}

		public EventRepeatDaily.Data build() {
			return new EventRepeatDailyDataImpl($builder.build());
		}
	}

	public static EventRepeatDaily.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}
