package at.bestsolution.quti.client.jdkhttp.impl.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import at.bestsolution.quti.client.model.Event;
import at.bestsolution.quti.client.model._Base.Nillable;
import jakarta.json.JsonObject;

public class EventDataPatchImpl extends _BaseDataImpl implements Event.Patch {

	EventDataPatchImpl(JsonObject data) {
		super(data);
	}

	@Override
	public Optional<String> title() {
		return _JsonUtils.mapOptString(data, "title");
	}

	@Override
	public Nillable<String> description() {
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
	public Nillable<Boolean> fullday() {
		return _JsonUtils.mapNilBoolean(data, "fullday");
	}

	@Override
	public Nillable<at.bestsolution.quti.client.model.EventRepeat.Data> repeat() {
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

}
