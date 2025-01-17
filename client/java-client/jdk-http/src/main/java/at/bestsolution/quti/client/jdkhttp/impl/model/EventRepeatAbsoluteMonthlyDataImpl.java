// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.model;

import java.time.LocalDate;
import java.time.ZoneId;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.model.EventRepeatAbsoluteMonthly;

public class EventRepeatAbsoluteMonthlyDataImpl extends _BaseDataImpl implements EventRepeatAbsoluteMonthly.Data {
	EventRepeatAbsoluteMonthlyDataImpl(JsonObject data) {
		super(data);
	}

	public short dayOfMonth() {
		return _JsonUtils.mapShort(data, "dayOfMonth");
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

	public static EventRepeatAbsoluteMonthly.Data of(JsonObject obj) {
		return new EventRepeatAbsoluteMonthlyDataImpl(obj);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public static class DataBuilderImpl implements EventRepeatAbsoluteMonthly.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public DataBuilderImpl() {
			$builder.add("@type", "absolute-monthly");
		}

		public EventRepeatAbsoluteMonthly.DataBuilder dayOfMonth(short dayOfMonth) {
			$builder.add("dayOfMonth", dayOfMonth);
			return this;
		}

		public EventRepeatAbsoluteMonthly.DataBuilder interval(short interval) {
			$builder.add("interval", interval);
			return this;
		}

		public EventRepeatAbsoluteMonthly.DataBuilder endDate(LocalDate endDate) {
			if (endDate == null) {
				return this;
			}
			$builder.add("endDate", endDate.toString());
			return this;
		}

		public EventRepeatAbsoluteMonthly.DataBuilder timeZone(ZoneId timeZone) {
			if (timeZone == null) {
				return this;
			}
			$builder.add("timeZone", timeZone.toString());
			return this;
		}

		public EventRepeatAbsoluteMonthly.Data build() {
			return new EventRepeatAbsoluteMonthlyDataImpl($builder.build());
		}
	}

	public static EventRepeatAbsoluteMonthly.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}