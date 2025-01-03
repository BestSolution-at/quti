// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp.impl.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import at.bestsolution.quti.client.dto.EventRepeatRelativeYearlyDTO;

public class EventRepeatRelativeYearlyDTOImpl extends BaseDTOImpl implements EventRepeatRelativeYearlyDTO {

    EventRepeatRelativeYearlyDTOImpl(JsonObject data) {
        super(data);
    }

    @Override
    public List<DayOfWeek> daysOfWeek() {
        return DTOUtils.mapLiterals(data, "daysOfWeek", DayOfWeek::valueOf);
    }

    @Override
    public Month month() {
        return DTOUtils.mapLiteral(data, "month", Month::valueOf);
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

    public static EventRepeatRelativeYearlyDTO of(JsonObject data) {
        return new EventRepeatRelativeYearlyDTOImpl(data);
    }

    public static List<EventRepeatRelativeYearlyDTO> of(JsonArray data) {
        return DTOUtils.mapObjects(data, EventRepeatRelativeYearlyDTOImpl::of);
    }

    public static class BuilderImpl implements Builder {
        private JsonObjectBuilder $builder = Json.createObjectBuilder();
        public BuilderImpl() {
            $builder.add("@type", "relative-yearly");
        }
        @Override
        public Builder daysOfWeek(List<DayOfWeek> daysOfWeek) {
            $builder.add("daysOfWeek", DTOUtils.toJsonLiteralArray(daysOfWeek));
            return this;
        }

        @Override
        public Builder month(Month month) {
            $builder.add("month", month.toString());
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

        public EventRepeatRelativeYearlyDTO build() {
            return new EventRepeatRelativeYearlyDTOImpl($builder.build());
        }
    }
}
