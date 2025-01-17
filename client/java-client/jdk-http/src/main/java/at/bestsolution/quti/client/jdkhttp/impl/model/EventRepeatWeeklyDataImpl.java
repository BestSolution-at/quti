// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.model.EventRepeatWeekly;

public class EventRepeatWeeklyDataImpl extends _BaseDataImpl implements EventRepeatWeekly.Data {
	EventRepeatWeeklyDataImpl(JsonObject data) {
		super(data);
	}

	public List<DayOfWeek> daysOfWeek() {
		return _JsonUtils.mapLiterals(data, "daysOfWeek", DayOfWeek::valueOf);
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

	public static EventRepeatWeekly.Data of(JsonObject obj) {
		return new EventRepeatWeeklyDataImpl(obj);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public static class DataBuilderImpl implements EventRepeatWeekly.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public DataBuilderImpl() {
			$builder.add("@type", "weekly");
		}

		public EventRepeatWeekly.DataBuilder daysOfWeek(List<DayOfWeek> daysOfWeek) {
			if (daysOfWeek == null) {
				return this;
			}
			$builder.add("daysOfWeek", _JsonUtils.toJsonLiteralArray(daysOfWeek));
			return this;
		}

		public EventRepeatWeekly.DataBuilder interval(short interval) {
			$builder.add("interval", interval);
			return this;
		}

		public EventRepeatWeekly.DataBuilder endDate(LocalDate endDate) {
			if (endDate == null) {
				return this;
			}
			$builder.add("endDate", endDate.toString());
			return this;
		}

		public EventRepeatWeekly.DataBuilder timeZone(ZoneId timeZone) {
			if (timeZone == null) {
				return this;
			}
			$builder.add("timeZone", timeZone.toString());
			return this;
		}

		public EventRepeatWeekly.Data build() {
			return new EventRepeatWeeklyDataImpl($builder.build());
		}
	}

	public static EventRepeatWeekly.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}