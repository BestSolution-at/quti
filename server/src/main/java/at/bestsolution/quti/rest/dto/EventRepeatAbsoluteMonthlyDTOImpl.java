// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.service.dto.EventRepeatAbsoluteMonthlyDTO;

public class EventRepeatAbsoluteMonthlyDTOImpl extends EventRepeatDTOImpl implements EventRepeatAbsoluteMonthlyDTO {
    public short dayOfMonth;

    public short dayOfMonth() {
        return this.dayOfMonth;
    }

    public static EventRepeatAbsoluteMonthlyDTOImpl of(EventRepeatAbsoluteMonthlyDTO source) {
        if(source == null) {
            return null;
        }
        else if(source instanceof EventRepeatAbsoluteMonthlyDTOImpl) {
            return (EventRepeatAbsoluteMonthlyDTOImpl)source;
        }
        var rv = new EventRepeatAbsoluteMonthlyDTOImpl();
        rv.dayOfMonth = source.dayOfMonth();
        rv.interval = source.interval();
        rv.endDate = source.endDate();
        rv.timeZone = source.timeZone();
        return rv;
    }
    public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl implements EventRepeatAbsoluteMonthlyDTO.Builder {
        public short dayOfMonth;
        public short interval;
        public LocalDate endDate;
        public ZoneId timeZone;

        public EventRepeatAbsoluteMonthlyDTO.Builder dayOfMonth(short dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
            return this;
        }
        public EventRepeatAbsoluteMonthlyDTO.Builder interval(short interval) {
            this.interval = interval;
            return this;
        }
        public EventRepeatAbsoluteMonthlyDTO.Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        public EventRepeatAbsoluteMonthlyDTO.Builder timeZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public at.bestsolution.quti.service.dto.EventRepeatAbsoluteMonthlyDTO build() {
            var rv = new EventRepeatAbsoluteMonthlyDTOImpl();
            rv.dayOfMonth = dayOfMonth;
            rv.interval = interval;
            rv.endDate = endDate;
            rv.timeZone = timeZone;
            return rv;
        }
    }

    public static EventRepeatAbsoluteMonthlyDTO.Builder builder() {
        return new BuilderImpl();
    }
}