// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.model;

import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.List;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.service.model._Base;
import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventRepeat;
import at.bestsolution.quti.service.model.EventRepeatAbsoluteMonthly;
import at.bestsolution.quti.service.model.EventRepeatAbsoluteYearly;
import at.bestsolution.quti.service.model.EventRepeatDaily;
import at.bestsolution.quti.service.model.EventRepeatRelativeMonthly;
import at.bestsolution.quti.service.model.EventRepeatRelativeYearly;
import at.bestsolution.quti.service.model.EventRepeatWeekly;

public class EventDataPatchImpl extends _BaseDataImpl implements Event.Patch {
	EventDataPatchImpl(JsonObject data) {
		super(data);
	}

	public Optional<String> title() {
		return _JsonUtils.mapOptString(data, "title");
	}

	public _Base.Nillable<String> description() {
		return _JsonUtils.mapNilString(data, "description");
	}

	public Optional<ZonedDateTime> start() {
		return _JsonUtils.mapOptZonedDateTime(data, "start");
	}

	public Optional<ZonedDateTime> end() {
		return _JsonUtils.mapOptZonedDateTime(data, "end");
	}

	public _Base.Nillable<Boolean> fullday() {
		return _JsonUtils.mapNilBoolean(data, "fullday");
	}

	public _Base.Nillable<EventRepeat.Data> repeat() {
		return _JsonUtils.mapNilObject(data, "repeat", EventRepeatDataImpl::of);
	}

	public Optional<List<String>> tags() {
		return _JsonUtils.mapOptStrings(data, "tags");
	}

	public Optional<List<String>> referencedCalendars() {
		return _JsonUtils.mapOptStrings(data, "referencedCalendars");
	}

	public static class PatchBuilderImpl implements Event.PatchBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		@Override
		public PatchBuilder title(String title) {
			$builder.add("title", title);
			return this;
		}

		@Override
		public PatchBuilder description(String description) {
			if (description == null) {
				$builder.addNull("description");
				return this;
			}
			$builder.add("description", description);
			return this;
		}

		@Override
		public PatchBuilder start(ZonedDateTime start) {
			$builder.add("start", start.toString());
			return this;
		}

		@Override
		public PatchBuilder end(ZonedDateTime end) {
			$builder.add("end", end.toString());
			return this;
		}

		@Override
		public PatchBuilder fullday(Boolean fullday) {
			if (fullday == null) {
				$builder.addNull("fullday");
				return this;
			}
			$builder.add("fullday", fullday);
			return this;
		}

		@Override
		public PatchBuilder repeat(EventRepeat.Data repeat) {
			if (repeat == null) {
				$builder.addNull("repeat");
				return this;
			}
			$builder.add("repeat", ((_BaseDataImpl)repeat).data);
			return this;
		}

		public <T extends EventRepeat.DataBuilder> PatchBuilder withRepeat(Class<T> clazz, Function<T, EventRepeat.Data> block) {
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
				throw new IllegalArgumentException("Unknown builder type %s".formatted(clazz));
			}
			return repeat(block.apply(clazz.cast(b)));
		}

		@Override
		public PatchBuilder tags(List<String> tags) {
			$builder.add("tags", _JsonUtils.toJsonStringArray(tags));
			return this;
		}

		@Override
		public PatchBuilder referencedCalendars(List<String> referencedCalendars) {
			$builder.add("referencedCalendars", _JsonUtils.toJsonStringArray(referencedCalendars));
			return this;
		}

		@Override
		public Patch build() {
			return new EventDataPatchImpl($builder.build());}
	}

	public static PatchBuilderImpl builder() {
		return new PatchBuilderImpl();
	}
}
