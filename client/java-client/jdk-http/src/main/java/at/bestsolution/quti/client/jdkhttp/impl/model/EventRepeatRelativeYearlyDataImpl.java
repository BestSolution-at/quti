// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.model.EventRepeatRelativeYearly;

public class EventRepeatRelativeYearlyDataImpl extends _BaseDataImpl implements EventRepeatRelativeYearly.Data {
	EventRepeatRelativeYearlyDataImpl(JsonObject data) {
		super(data);
	}

	public List<DayOfWeek> daysOfWeek() {
		return _JsonUtils.mapLiterals(data, "daysOfWeek", DayOfWeek::valueOf);
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

	public static EventRepeatRelativeYearly.Data of(JsonObject obj) {
		return new EventRepeatRelativeYearlyDataImpl(obj);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public static class DataBuilderImpl implements EventRepeatRelativeYearly.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public DataBuilderImpl() {
			$builder.add("@type", "relative-yearly");
		}

		public EventRepeatRelativeYearly.DataBuilder daysOfWeek(List<DayOfWeek> daysOfWeek) {
			if (daysOfWeek == null) {
				return this;
			}
			$builder.add("daysOfWeek", _JsonUtils.toJsonLiteralArray(daysOfWeek));
			return this;
		}

		public EventRepeatRelativeYearly.DataBuilder month(Month month) {
			if (month == null) {
				return this;
			}
			$builder.add("month", month.toString());
			return this;
		}

		public EventRepeatRelativeYearly.DataBuilder interval(short interval) {
			$builder.add("interval", interval);
			return this;
		}

		public EventRepeatRelativeYearly.DataBuilder endDate(LocalDate endDate) {
			if (endDate == null) {
				return this;
			}
			$builder.add("endDate", endDate.toString());
			return this;
		}

		public EventRepeatRelativeYearly.DataBuilder timeZone(ZoneId timeZone) {
			if (timeZone == null) {
				return this;
			}
			$builder.add("timeZone", timeZone.toString());
			return this;
		}

		public EventRepeatRelativeYearly.Data build() {
			return new EventRepeatRelativeYearlyDataImpl($builder.build());
		}
	}

	public static EventRepeatRelativeYearly.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}
