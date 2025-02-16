// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.model._Base;
import at.bestsolution.quti.client.model.EventRepeatRelativeYearly;

public class EventRepeatRelativeYearlyDataPatchImpl extends _BaseDataImpl implements EventRepeatRelativeYearly.Patch {
	EventRepeatRelativeYearlyDataPatchImpl(JsonObject data) {
		super(data);
	}

	public Optional<List<DayOfWeek>> daysOfWeek() {
		return _JsonUtils.mapOptLiterals(data, "daysOfWeek", DayOfWeek::valueOf );
	}

	public Optional<Month> month() {
		return _JsonUtils.mapOptLiteral(data, "month", Month::valueOf );
	}

	public Optional<Short> interval() {
		return _JsonUtils.mapOptShort(data, "interval");
	}

	public _Base.Nillable<LocalDate> endDate() {
		return _JsonUtils.mapNilLocalDate(data, "endDate");
	}

	public Optional<ZoneId> timeZone() {
		return _JsonUtils.mapOptLiteral(data, "timeZone", ZoneId::of );
	}

	public static class PatchBuilderImpl implements EventRepeatRelativeYearly.PatchBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		@Override
		public PatchBuilder daysOfWeek(List<DayOfWeek> daysOfWeek) {
			;
			return this;
		}

		@Override
		public PatchBuilder month(Month month) {
			;
			return this;
		}

		@Override
		public PatchBuilder interval(short interval) {
			$builder.add("interval", interval);
			return this;
		}

		@Override
		public PatchBuilder endDate(LocalDate endDate) {
			if (endDate == null) {
				$builder.addNull("endDate");
				return this;
			}
			$builder.add("endDate", endDate.toString());
			return this;
		}

		@Override
		public PatchBuilder timeZone(ZoneId timeZone) {
			$builder.add("timeZone", timeZone.toString());
			return this;
		}

		@Override
		public Patch build() {
			return new EventRepeatRelativeYearlyDataPatchImpl($builder.build());
		}
	}

	public static EventRepeatRelativeYearly.Patch of(JsonObject obj) {
		return new EventRepeatRelativeYearlyDataPatchImpl(obj);
	}

	public static PatchBuilderImpl builder() {
		return new PatchBuilderImpl();
	}
}
