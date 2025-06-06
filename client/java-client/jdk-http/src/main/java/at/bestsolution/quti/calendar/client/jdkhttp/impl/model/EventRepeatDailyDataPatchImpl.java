// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.client.jdkhttp.impl.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.calendar.client.model._Base;
import at.bestsolution.quti.calendar.client.model.EventRepeatDaily;

public class EventRepeatDailyDataPatchImpl extends _BaseDataImpl implements EventRepeatDaily.Patch {
	EventRepeatDailyDataPatchImpl(JsonObject data) {
		super(data);
	}

	public Optional<Short> interval() {
		return _JsonUtils.mapOptShort(data, "interval");
	}

	public _Base.Nillable<LocalDate> endDate() {
		return _JsonUtils.mapNilLocalDate(data, "endDate");
	}

	public Optional<ZoneId> timeZone() {
		return _JsonUtils.mapOptLiteral(data, "timeZone", ZoneId::of);
	}

	public static class PatchBuilderImpl implements EventRepeatDaily.PatchBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

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
			return new EventRepeatDailyDataPatchImpl($builder.build());
		}
	}

	public static EventRepeatDaily.Patch of(JsonObject obj) {
		return new EventRepeatDailyDataPatchImpl(obj);
	}

	public static PatchBuilderImpl builder() {
		return new PatchBuilderImpl();
	}
}
