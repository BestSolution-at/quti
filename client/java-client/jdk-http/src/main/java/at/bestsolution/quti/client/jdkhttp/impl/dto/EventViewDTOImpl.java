package at.bestsolution.quti.client.jdkhttp.impl.dto;

import java.time.ZonedDateTime;
import java.util.List;

import at.bestsolution.quti.client.dto.EventViewDTO;
import at.bestsolution.quti.client.jdkhttp.impl.Utils;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public abstract class EventViewDTOImpl extends BaseDTOImpl implements EventViewDTO {
    public EventViewDTOImpl(JsonObject data) {
        super(data);
    }

    public String key() {
        return data.getString("key");
    }

    public String calendarKey() {
        return data.getString("calendarKey");
    }

    public String title() {
        return data.getString("title");
    }

    public String description() {
        return data.getString("description");
    }

    public String owner() {
        return data.getString("owner");
    }

    public Status status() {
        return Status.valueOf(data.getString("status"));
    }

    public ZonedDateTime start() {
        return ZonedDateTime.parse(data.getString("start"));
    }

    public ZonedDateTime end() {
        return ZonedDateTime.parse(data.getString("end"));
    }

    public List<String> tags() {
        return Utils.mapStrings(data, "tags");
    }

    public List<String> referencedCalendars() {
        return Utils.mapStrings(data, "referencedCalendars");
    }

    public static EventViewDTO of(JsonObject data) {
        return switch (data.getString("@type")) {
            case "single" -> new SingleEventViewDTOImpl(data);
            case "series-moved" -> new SeriesMovedEventViewDTOImpl(data);
            case "series" -> new SeriesEventViewDTOImpl(data);
            default ->
                throw new IllegalArgumentException("Unexpected value: " + data.getString("@type"));
        };
    }

    public static List<EventViewDTO> of(JsonArray data) {
        return Utils.mapObjects(data, EventViewDTOImpl::of);
    }

    static class SingleEventViewDTOImpl extends EventViewDTOImpl implements SingleEventViewDTO {
        SingleEventViewDTOImpl(JsonObject data) {
            super(data);
        }
    }

    static class SeriesMovedEventViewDTOImpl extends EventViewDTOImpl implements SeriesMovedEventViewDTO {
        SeriesMovedEventViewDTOImpl(JsonObject data) {
            super(data);
        }

        public String masterEventKey() {
            return data.getString("masterEventKey");
        }

        public ZonedDateTime originalStart() {
            return ZonedDateTime.parse(data.getString("originalStart"));
        }

        public ZonedDateTime originalEnd() {
            return ZonedDateTime.parse(data.getString("originalEnd"));
        }

    }

    public static class SeriesEventViewDTOImpl extends EventViewDTOImpl implements SeriesEventViewDTO {
        SeriesEventViewDTOImpl(JsonObject data) {
            super(data);
        }

        public String masterEventKey() {
            return data.getString("masterEventKey");
        }
    }
}
