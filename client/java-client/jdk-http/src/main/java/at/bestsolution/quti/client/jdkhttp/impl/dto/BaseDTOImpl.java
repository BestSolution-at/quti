package at.bestsolution.quti.client.jdkhttp.impl.dto;

import jakarta.json.JsonObject;

public abstract class BaseDTOImpl {
    public final JsonObject data;

    public BaseDTOImpl(JsonObject data) {
        this.data = data;
    }
}
