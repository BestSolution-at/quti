package at.bestsolution.quti.client.jdkhttp.impl.dto;

import at.bestsolution.quti.client.dto.CalendarNewDTO;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class CalendarNewDTOImpl extends BaseDTOImpl implements CalendarNewDTO {

    public CalendarNewDTOImpl(JsonObject data) {
        super(data);
    }

    @Override
    public String name() {
        return data.getString("name");
    }

    @Override
    public String owner() {
        return data.getString("owner");
    }

    public static CalendarNewDTO of(JsonObject data) {
		return new CalendarNewDTOImpl(data);
	}

	public static class BuilderImpl implements Builder {
		private JsonObjectBuilder builder = Json.createObjectBuilder();
		
		public Builder name(String name) {
			builder.add("name", name);
			return this;
		}
		
		public Builder owner(String owner) {
			builder.add("owner", owner);
			return this;
		}
		
		public CalendarNewDTO build() {
			return new CalendarNewDTOImpl(builder.build());
		}
	}
}
