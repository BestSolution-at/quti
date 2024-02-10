package at.bestsolution.qutime.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import at.bestsolution.qutime.model.EventRepeatAbsoluteMonthlyEntity;
import at.bestsolution.qutime.model.EventRepeatAbsoluteYearlyEntity;
import at.bestsolution.qutime.model.EventRepeatDailyEntity;
import at.bestsolution.qutime.model.EventRepeatEntity;
import at.bestsolution.qutime.model.EventRepeatRelativeMonthlyEntity;
import at.bestsolution.qutime.model.EventRepeatRelativeYearlyEntity;
import at.bestsolution.qutime.model.EventRepeatWeeklyEntity;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

@JsonbTypeInfo({
    @JsonbSubtype( alias = "daily", type = EventRepeatDTO.EventRepeatDailyDTO.class ),
    @JsonbSubtype( alias = "weekly", type = EventRepeatDTO.EventRepeatWeeklyDTO.class ),
    @JsonbSubtype( alias = "absolute-monthly", type = EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO.class ),
    @JsonbSubtype( alias = "absolute-monthly", type = EventRepeatDTO.EventRepeatAbsoluteYearlyDTO.class ),
    @JsonbSubtype( alias = "relative-monthly", type = EventRepeatDTO.EventRepeatRelativeMonthlyDTO.class ),
    @JsonbSubtype( alias = "relative-yearly", type = EventRepeatDTO.EventRepeatRelativeYearlyDTO.class ),
})
public abstract class EventRepeatDTO {
    public short interval;
    public LocalDate endDate;
    public String timeZone;

    public static class EventRepeatDailyDTO extends EventRepeatDTO {
        // nothing

        public static EventRepeatDailyDTO of(EventRepeatDailyEntity entity) {
            var result = new EventRepeatDailyDTO();
            result.interval = entity.interval;
            result.endDate = entity.endDate != null ? entity.endDate.toLocalDate() : null;
            result.timeZone = entity.recurrenceTimezone.toString();
            return result;
        }
    }

    public static class EventRepeatWeeklyDTO extends EventRepeatDTO {
        public List<DayOfWeek> daysOfWeek;

        public static EventRepeatWeeklyDTO of(EventRepeatWeeklyEntity entity) {
            var result = new EventRepeatWeeklyDTO();
            result.interval = entity.interval;
            result.endDate = entity.endDate.toLocalDate();
            result.timeZone = entity.recurrenceTimezone.toString();
            result.daysOfWeek = entity.daysOfWeek;
            return result;
        }
    }

    public static class EventRepeatAbsoluteMonthlyDTO extends EventRepeatDTO {
        public short dayOfMonth;

        public static EventRepeatAbsoluteMonthlyDTO of(EventRepeatAbsoluteMonthlyEntity entity) {
            var result = new EventRepeatAbsoluteMonthlyDTO();
            result.interval = entity.interval;
            result.endDate = entity.endDate.toLocalDate();
            result.timeZone = entity.recurrenceTimezone.toString();
            result.dayOfMonth = entity.dayOfMonth;
            return result;
        }
    }

    public static class EventRepeatAbsoluteYearlyDTO extends EventRepeatDTO {
        public short dayOfMonth;
        public Month month;

        public static EventRepeatAbsoluteYearlyDTO of(EventRepeatAbsoluteYearlyEntity entity) {
            var result = new EventRepeatAbsoluteYearlyDTO();
            result.interval = entity.interval;
            result.endDate = entity.endDate.toLocalDate();
            result.timeZone = entity.recurrenceTimezone.toString();
            result.dayOfMonth = entity.dayOfMonth;
            result.month = entity.month;
            return result;
        }
    }

    public static class EventRepeatRelativeMonthlyDTO extends EventRepeatDTO {
        public List<DayOfWeek> daysOfWeek;

        public static EventRepeatRelativeMonthlyDTO of(EventRepeatRelativeMonthlyEntity entity) {
            var result = new EventRepeatRelativeMonthlyDTO();
            result.interval = entity.interval;
            result.endDate = entity.endDate.toLocalDate();
            result.timeZone = entity.recurrenceTimezone.toString();
            result.daysOfWeek = entity.daysOfWeek;
            return result;
        }
    }

    public static class EventRepeatRelativeYearlyDTO extends EventRepeatDTO {
        public List<DayOfWeek> daysOfWeek;
        public Month month;

        public static EventRepeatRelativeYearlyDTO of(EventRepeatRelativeYearlyEntity entity) {
            var result = new EventRepeatRelativeYearlyDTO();
            result.interval = entity.interval;
            result.endDate = entity.endDate.toLocalDate();
            result.timeZone = entity.recurrenceTimezone.toString();
            result.daysOfWeek = entity.daysOfWeek;
            result.month = entity.month; 
            return result;
        }
    }

    public static EventRepeatDTO of(EventRepeatEntity entity) {
        if( entity == null ) {
            return null;
        }

        if( entity instanceof EventRepeatAbsoluteMonthlyEntity e ) {
            return EventRepeatAbsoluteMonthlyDTO.of(e);
        } else if( entity instanceof EventRepeatAbsoluteYearlyEntity e ) {
            return EventRepeatAbsoluteYearlyDTO.of(e);
        } else if( entity instanceof EventRepeatDailyEntity e ) {
            return EventRepeatDailyDTO.of(e);
        } else if( entity instanceof EventRepeatRelativeMonthlyEntity e ) {
            return EventRepeatRelativeMonthlyDTO.of(e);
        } else if( entity instanceof EventRepeatRelativeYearlyEntity e ) {
            return EventRepeatRelativeYearlyDTO.of(e);
        } else if( entity instanceof EventRepeatWeeklyEntity e ) {
            return EventRepeatWeeklyDTO.of(e);
        }
        throw new IllegalArgumentException("Unknown entity '"+entity+"'");
    }
}
