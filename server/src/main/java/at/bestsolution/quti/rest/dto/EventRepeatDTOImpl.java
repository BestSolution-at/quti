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
    @JsonbSubtype(alias = "daily", type = EventRepeatDTOImpl.EventRepeatDailyDTOImpl.class),
    @JsonbSubtype(alias = "weekly", type = EventRepeatDTOImpl.EventRepeatWeeklyDTOImpl.class),
    @JsonbSubtype(alias = "absolute-monthly", type = EventRepeatDTOImpl.EventRepeatAbsoluteMonthlyDTOImpl.class),
    @JsonbSubtype(alias = "absolute-yearly", type = EventRepeatDTOImpl.EventRepeatAbsoluteYearlyDTOImpl.class),
    @JsonbSubtype(alias = "relative-monthly", type = EventRepeatDTOImpl.EventRepeatRelativeMonthlyDTOImpl.class),
    @JsonbSubtype(alias = "relative-yearly", type = EventRepeatDTOImpl.EventRepeatRelativeYearlyDTOImpl.class),
})
public abstract class EventRepeatDTOImpl implements at.bestsolution.quti.service.dto.EventRepeatDTO {
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

    public static class EventRepeatDailyDTOImpl extends EventRepeatDTOImpl implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatDailyDTO {
    }

    public static class EventRepeatWeeklyDTOImpl extends EventRepeatDTOImpl implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatWeeklyDTO {
        public List<DayOfWeek> daysOfWeek;

        public List<DayOfWeek> daysOfWeek() {
            return this.daysOfWeek;
        }
    }

    public static class EventRepeatAbsoluteMonthlyDTOImpl extends EventRepeatDTOImpl implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO {
        public short dayOfMonth;

        public short dayOfMonth() {
            return this.dayOfMonth;
        }
    }

    public static class EventRepeatAbsoluteYearlyDTOImpl extends EventRepeatDTOImpl implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatAbsoluteYearlyDTO {
        public short dayOfMonth;
        public Month month;

        public short dayOfMonth() {
            return this.dayOfMonth;
        }

        public Month month() {
            return this.month;
        }
    }

    public static class EventRepeatRelativeMonthlyDTOImpl extends EventRepeatDTOImpl implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatRelativeMonthlyDTO {
        public List<DayOfWeek> daysOfWeek;

        public List<DayOfWeek> daysOfWeek() {
            return this.daysOfWeek;
        }
    }

    public static class EventRepeatRelativeYearlyDTOImpl extends EventRepeatDTOImpl implements at.bestsolution.quti.service.dto.EventRepeatDTO.EventRepeatRelativeYearlyDTO {
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