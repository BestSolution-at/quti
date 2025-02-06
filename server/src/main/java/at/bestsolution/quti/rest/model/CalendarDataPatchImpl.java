package at.bestsolution.quti.rest.model;

import java.util.Optional;

import at.bestsolution.quti.service.model.Calendar;
import at.bestsolution.quti.service.model._Base;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class CalendarDataPatchImpl extends _BaseDataImpl implements Calendar.Patch {
	CalendarDataPatchImpl(JsonObject data) {
		super(data);
	}

	@Override
	public Optional<String> name() {
		return _JsonUtils.mapOptString(data, "name");
	}

	@Override
	public _Base.Nillable<String> owner() {
		return _JsonUtils.mapNilString(data, "owner");
	}

	public static Calendar.PatchBuilder builder() {
		return new PatchBuilderImpl();
	}

	public static class PatchBuilderImpl implements Calendar.PatchBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		@Override
		public PatchBuilder name(String name) {
			$builder.add("name", name);
			return this;
		}

		@Override
		public PatchBuilder owner(String owner) {
			if (owner == null) {
				$builder.addNull("owner");
				return this;
			}
			$builder.add("owner", owner);
			return this;
		}

		@Override
		public Patch build() {
			return new CalendarDataPatchImpl($builder.build());
		}
	}
}
