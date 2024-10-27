// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

@JsonbTypeInfo({
    @JsonbSubtype(alias = "daily", type = EventRepeatDTO.EventRepeatDailyDTO.class),
    @JsonbSubtype(alias = "weekly", type = EventRepeatDTO.EventRepeatWeeklyDTO.class),
    @JsonbSubtype(alias = "absolute-monthly", type = EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO.class),
    @JsonbSubtype(alias = "absolute-yearly", type = EventRepeatDTO.EventRepeatAbsoluteYearlyDTO.class),
    @JsonbSubtype(alias = "relative-monthly", type = EventRepeatDTO.EventRepeatRelativeMonthlyDTO.class),
    @JsonbSubtype(alias = "relative-yearly", type = EventRepeatDTO.EventRepeatRelativeYearlyDTO.class),
})
public abstract class EventRepeatDTO implements at.bestsolution.quti.service.dto.EventRepeatDTO {
    public short interval;
    public LocalDate endDate;
    public ZoneId timeZone;

    public short interval() {
        return this.interval;
    }

    public LocalDate endDate() {
        return this.endDate;
    }

    public ZoneId timeZone() {
        return this.timeZone;
    }

    public static class EventRepeatDailyDTO extends EventRepeatDTO implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatDailyDTO {
    }

    public static class EventRepeatWeeklyDTO extends EventRepeatDTO implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatWeeklyDTO {
        public List<DayOfWeek> daysOfWeek;

        public List<DayOfWeek> daysOfWeek() {
            return this.daysOfWeek;
        }
    }

    public static class EventRepeatAbsoluteMonthlyDTO extends EventRepeatDTO implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO {
        public short dayOfMonth;

        public short dayOfMonth() {
            return this.dayOfMonth;
        }
    }

    public static class EventRepeatAbsoluteYearlyDTO extends EventRepeatDTO implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatAbsoluteYearlyDTO {
        public short dayOfMonth;
        public Month month;

        public short dayOfMonth() {
            return this.dayOfMonth;
        }

        public Month month() {
            return this.month;
        }
    }

    public static class EventRepeatRelativeMonthlyDTO extends EventRepeatDTO implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatRelativeMonthlyDTO {
        public List<DayOfWeek> daysOfWeek;

        public List<DayOfWeek> daysOfWeek() {
            return this.daysOfWeek;
        }
    }

    public static class EventRepeatRelativeYearlyDTO extends EventRepeatDTO implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatRelativeYearlyDTO {
        public List<DayOfWeek> daysOfWeek;
        public Month month;

        public List<DayOfWeek> daysOfWeek() {
            return this.daysOfWeek;
        }

        public Month month() {
            return this.month;
        }
    }
}