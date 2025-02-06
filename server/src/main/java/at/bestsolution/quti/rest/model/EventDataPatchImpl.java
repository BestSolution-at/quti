package at.bestsolution.quti.rest.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventRepeat;
import at.bestsolution.quti.service.model._Base;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class EventDataPatchImpl extends _BaseDataImpl implements Event.Patch {

	EventDataPatchImpl(JsonObject data) {
		super(data);
	}

	@Override
	public Optional<String> title() {
		return _JsonUtils.mapOptString(data, "title");
	}

	@Override
	public _Base.Nillable<String> description() {
		return _JsonUtils.mapNilString(data, "description");
	}

	@Override
	public Optional<ZonedDateTime> start() {
		return _JsonUtils.mapOptZonedDateTime(data, "start");
	}

	@Override
	public Optional<ZonedDateTime> end() {
		return _JsonUtils.mapOptZonedDateTime(data, "end");
	}

	@Override
	public _Base.Nillable<Boolean> fullday() {
		return _JsonUtils.mapNilBoolean(data, "fullday");
	}

	@Override
	public _Base.Nillable<EventRepeat.Data> repeat() {
		return _JsonUtils.mapNilObject(data, "repeat", EventRepeatDataImpl::of);
	}

	@Override
	public Optional<List<String>> tags() {
		return _JsonUtils.mapOptStrings(data, "tags");
	}

	@Override
	public Optional<List<String>> referencedCalendars() {
		return _JsonUtils.mapOptStrings(data, "referencedCalendars");
	}

	public static Event.PatchBuilder builder() {
		return new PatchBuilderImpl();
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
		public PatchBuilder repeat(at.bestsolution.quti.service.model.EventRepeat.Data repeat) {
			if (repeat == null) {
				$builder.addNull("repeat");
				return this;
			}
			$builder.add("repeat", ((_BaseDataImpl) repeat).data);
			return this;
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
			return new EventDataPatchImpl($builder.build());
		}

	}
}
