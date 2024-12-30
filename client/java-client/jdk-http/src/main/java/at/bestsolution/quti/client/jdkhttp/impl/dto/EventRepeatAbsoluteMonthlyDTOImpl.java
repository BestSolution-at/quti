// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.dto.EventRepeatAbsoluteMonthlyDTO;

public class EventRepeatAbsoluteMonthlyDTOImpl extends BaseDTOImpl implements EventRepeatAbsoluteMonthlyDTO {

    EventRepeatAbsoluteMonthlyDTOImpl(JsonObject data) {
        super(data);
    }

    @Override
    public short dayOfMonth() {
        return DTOUtils.mapShort(data, "dayOfMonth");
    }

    @Override
    public short interval() {
        return DTOUtils.mapShort(data, "interval");
    }

    @Override
    public LocalDate endDate() {
        return DTOUtils.mapLocalDate(data, "endDate", null);
    }

    @Override
    public ZoneId timeZone() {
        return DTOUtils.mapLiteral(data, "timeZone", ZoneId::of);
    }

    public static EventRepeatAbsoluteMonthlyDTO of(JsonObject data) {
        return new EventRepeatAbsoluteMonthlyDTOImpl(data);
    }

    public static List<EventRepeatAbsoluteMonthlyDTO> of(JsonArray data) {
        return DTOUtils.mapObjects(data, EventRepeatAbsoluteMonthlyDTOImpl::of);
    }

    public static class BuilderImpl implements Builder {
        private JsonObjectBuilder $builder = Json.createObjectBuilder();
        public BuilderImpl() {
            $builder.add("@type", "absolute-monthly");
        }
        @Override
        public Builder dayOfMonth(short dayOfMonth) {
            $builder.add("dayOfMonth", dayOfMonth);
            return this;
        }

        @Override
        public Builder interval(short interval) {
            $builder.add("interval", interval);
            return this;
        }

        @Override
        public Builder endDate(LocalDate endDate) {
            if( endDate == null ) {
                return this;
            }
            $builder.add("endDate", endDate.toString());
            return this;
        }

        @Override
        public Builder timeZone(ZoneId timeZone) {
            $builder.add("timeZone", timeZone.toString());
            return this;
        }

        public EventRepeatAbsoluteMonthlyDTO build() {
            return new EventRepeatAbsoluteMonthlyDTOImpl($builder.build());
        }
    }
}
