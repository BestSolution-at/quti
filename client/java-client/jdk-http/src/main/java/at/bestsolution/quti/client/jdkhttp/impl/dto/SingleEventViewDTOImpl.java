// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.dto;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.dto.SingleEventViewDTO;

public class SingleEventViewDTOImpl extends BaseDTOImpl implements SingleEventViewDTO {

    SingleEventViewDTOImpl(JsonObject data) {
        super(data);
    }

    @Override
    public String key() {
        return DTOUtils.mapString(data, "key");
    }

    @Override
    public String calendarKey() {
        return DTOUtils.mapString(data, "calendarKey");
    }

    @Override
    public String title() {
        return DTOUtils.mapString(data, "title");
    }

    @Override
    public String description() {
        return DTOUtils.mapString(data, "description");
    }

    @Override
    public String owner() {
        return DTOUtils.mapString(data, "owner");
    }

    @Override
    public Status status() {
        return DTOUtils.mapLiteral(data, "status", Status::valueOf);
    }

    @Override
    public ZonedDateTime start() {
        return DTOUtils.mapZonedDateTime(data, "start");
    }

    @Override
    public ZonedDateTime end() {
        return DTOUtils.mapZonedDateTime(data, "end");
    }

    @Override
    public List<String> tags() {
        return DTOUtils.mapStrings(data, "tags");
    }

    @Override
    public List<String> referencedCalendars() {
        return DTOUtils.mapStrings(data, "referencedCalendars");
    }

    public static SingleEventViewDTO of(JsonObject data) {
        return new SingleEventViewDTOImpl(data);
    }

    public static List<SingleEventViewDTO> of(JsonArray data) {
        return DTOUtils.mapObjects(data, SingleEventViewDTOImpl::of);
    }

    public static class BuilderImpl implements Builder {
        private JsonObjectBuilder $builder = Json.createObjectBuilder();
        public BuilderImpl() {
            $builder.add("@type", "single");
        }
        @Override
        public Builder key(String key) {
            $builder.add("key", key);
            return this;
        }

        @Override
        public Builder calendarKey(String calendarKey) {
            $builder.add("calendarKey", calendarKey);
            return this;
        }

        @Override
        public Builder title(String title) {
            $builder.add("title", title);
            return this;
        }

        @Override
        public Builder description(String description) {
            $builder.add("description", description);
            return this;
        }

        @Override
        public Builder owner(String owner) {
            $builder.add("owner", owner);
            return this;
        }

        @Override
        public Builder status(Status status) {
            $builder.add("status", status.toString());
            return this;
        }

        @Override
        public Builder start(ZonedDateTime start) {
            $builder.add("start", start.toString());
            return this;
        }

        @Override
        public Builder end(ZonedDateTime end) {
            $builder.add("end", end.toString());
            return this;
        }

        @Override
        public Builder tags(List<String> tags) {
            $builder.add("tags", DTOUtils.toJsonStringArray(tags));
            return this;
        }

        @Override
        public Builder referencedCalendars(List<String> referencedCalendars) {
            $builder.add("referencedCalendars", DTOUtils.toJsonStringArray(referencedCalendars));
            return this;
        }

        public SingleEventViewDTO build() {
            return new SingleEventViewDTOImpl($builder.build());
        }
    }
}