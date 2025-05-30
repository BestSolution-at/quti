// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.client.jdkhttp.impl.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.calendar.client.model.EventRepeatAbsoluteYearly;

public class EventRepeatAbsoluteYearlyDataImpl extends _BaseDataImpl implements EventRepeatAbsoluteYearly.Data {
	EventRepeatAbsoluteYearlyDataImpl(JsonObject data) {
		super(data);
	}

	public short dayOfMonth() {
		return _JsonUtils.mapShort(data, "dayOfMonth");
	}

	public Month month() {
		return _JsonUtils.mapLiteral(data, "month", Month::valueOf);
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

	public static EventRepeatAbsoluteYearly.Data of(JsonObject obj) {
		return new EventRepeatAbsoluteYearlyDataImpl(obj);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public static class DataBuilderImpl implements EventRepeatAbsoluteYearly.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public DataBuilderImpl() {
			$builder.add("@type", "absolute-yearly");
		}

		public EventRepeatAbsoluteYearly.DataBuilder dayOfMonth(short dayOfMonth) {
			$builder.add("dayOfMonth", dayOfMonth);
			return this;
		}

		public EventRepeatAbsoluteYearly.DataBuilder month(Month month) {
			if (month == null) {
				return this;
			}
			$builder.add("month", month.toString());
			return this;
		}

		public EventRepeatAbsoluteYearly.DataBuilder interval(short interval) {
			$builder.add("interval", interval);
			return this;
		}

		public EventRepeatAbsoluteYearly.DataBuilder endDate(LocalDate endDate) {
			if (endDate == null) {
				return this;
			}
			$builder.add("endDate", endDate.toString());
			return this;
		}

		public EventRepeatAbsoluteYearly.DataBuilder timeZone(ZoneId timeZone) {
			if (timeZone == null) {
				return this;
			}
			$builder.add("timeZone", timeZone.toString());
			return this;
		}

		public EventRepeatAbsoluteYearly.Data build() {
			return new EventRepeatAbsoluteYearlyDataImpl($builder.build());
		}
	}

	public static EventRepeatAbsoluteYearly.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}
