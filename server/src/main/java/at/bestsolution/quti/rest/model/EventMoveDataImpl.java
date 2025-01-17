// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.model;

import java.time.ZonedDateTime;

import jakarta.json.JsonObject;

public class EventMoveDataImpl extends _BaseDataImpl {
	public EventMoveDataImpl(JsonObject data) {
		super(data);
	}

	public ZonedDateTime start() {
		return _JsonUtils.mapZonedDateTime(data, "start");
	}

	public ZonedDateTime end() {
		return _JsonUtils.mapZonedDateTime(data, "end");
	}

}