package at.bestsolution.quti.client.jdkhttp.impl.model;

import java.util.Optional;

import at.bestsolution.quti.client.model.Calendar;
import at.bestsolution.quti.client.model._Base.Nillable;
import jakarta.json.JsonObject;

public class CalendarDataPatchImpl extends _BaseDataImpl implements Calendar.Patch {
	CalendarDataPatchImpl(JsonObject data) {
		super(data);
	}

	@Override
	public Optional<String> name() {
		return _JsonUtils.mapOptString(data, "name");
	}

	@Override
	public Nillable<String> owner() {
		return _JsonUtils.mapNilString(data, "name");
	}
}
