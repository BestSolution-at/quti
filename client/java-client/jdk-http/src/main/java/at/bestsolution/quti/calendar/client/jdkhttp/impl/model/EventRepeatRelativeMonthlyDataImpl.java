// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.client.jdkhttp.impl.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.calendar.client.model.EventRepeatRelativeMonthly;

public class EventRepeatRelativeMonthlyDataImpl extends _BaseDataImpl implements EventRepeatRelativeMonthly.Data {
	EventRepeatRelativeMonthlyDataImpl(JsonObject data) {
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

	public static EventRepeatRelativeMonthly.Data of(JsonObject obj) {
		return new EventRepeatRelativeMonthlyDataImpl(obj);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public static class DataBuilderImpl implements EventRepeatRelativeMonthly.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public DataBuilderImpl() {
			$builder.add("@type", "relative-monthly");
		}

		public EventRepeatRelativeMonthly.DataBuilder daysOfWeek(List<DayOfWeek> daysOfWeek) {
			if (daysOfWeek == null) {
				return this;
			}
			$builder.add("daysOfWeek", _JsonUtils.toJsonLiteralArray(daysOfWeek));
			return this;
		}

		public EventRepeatRelativeMonthly.DataBuilder interval(short interval) {
			$builder.add("interval", interval);
			return this;
		}

		public EventRepeatRelativeMonthly.DataBuilder endDate(LocalDate endDate) {
			if (endDate == null) {
				return this;
			}
			$builder.add("endDate", endDate.toString());
			return this;
		}

		public EventRepeatRelativeMonthly.DataBuilder timeZone(ZoneId timeZone) {
			if (timeZone == null) {
				return this;
			}
			$builder.add("timeZone", timeZone.toString());
			return this;
		}

		public EventRepeatRelativeMonthly.Data build() {
			return new EventRepeatRelativeMonthlyDataImpl($builder.build());
		}
	}

	public static EventRepeatRelativeMonthly.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}
