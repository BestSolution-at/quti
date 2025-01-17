// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.model;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.service.model.CalendarNew;

public class CalendarNewDataImpl extends _BaseDataImpl implements CalendarNew.Data {
	CalendarNewDataImpl(JsonObject data) {
		super(data);
	}

	public String name() {
		return _JsonUtils.mapString(data, "name");
	}

	public String owner() {
		return _JsonUtils.mapString(data, "owner", null);
	}

	public static CalendarNew.Data of(JsonObject obj) {
		return new CalendarNewDataImpl(obj);
	}

	public String toString() {
		return getClass().getSimpleName();
	}

	public static class DataBuilderImpl implements CalendarNew.DataBuilder {
		private JsonObjectBuilder $builder = Json.createObjectBuilder();

		public CalendarNew.DataBuilder name(String name) {
			if (name == null) {
				return this;
			}
			$builder.add("name", name);
			return this;
		}

		public CalendarNew.DataBuilder owner(String owner) {
			if (owner == null) {
				return this;
			}
			$builder.add("owner", owner);
			return this;
		}

		public CalendarNew.Data build() {
			return new CalendarNewDataImpl($builder.build());
		}
	}

	public static CalendarNew.DataBuilder builder() {
		return new DataBuilderImpl();
	}
}
