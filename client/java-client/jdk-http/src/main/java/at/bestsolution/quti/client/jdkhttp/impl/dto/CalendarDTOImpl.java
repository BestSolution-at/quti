package at.bestsolution.quti.client.jdkhttp.impl.dto;

import at.bestsolution.quti.client.dto.CalendarDTO;
import jakarta.json.JsonObject;

public class CalendarDTOImpl extends BaseDTOImpl implements CalendarDTO {

    public CalendarDTOImpl(JsonObject data) {
        super(data);
    }

    @Override
    public String key() {
        return data.getString("key");
    }

    @Override
    public String name() {
        return data.getString("name");
    }

    @Override
    public String owner() {
        return data.getString("owner");
    }

    public static CalendarDTO of(JsonObject data) {
        return new CalendarDTOImpl(data);
    }
}
