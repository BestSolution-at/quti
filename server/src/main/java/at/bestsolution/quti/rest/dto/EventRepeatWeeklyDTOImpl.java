// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.service.dto.EventRepeatWeeklyDTO;

public class EventRepeatWeeklyDTOImpl extends EventRepeatDTOImpl implements EventRepeatWeeklyDTO {
    public List<DayOfWeek> daysOfWeek;

    public List<DayOfWeek> daysOfWeek() {
        return this.daysOfWeek;
    }

    public static EventRepeatWeeklyDTOImpl of(EventRepeatWeeklyDTO source) {
        if(source == null) {
            return null;
        }
        else if(source instanceof EventRepeatWeeklyDTOImpl) {
            return (EventRepeatWeeklyDTOImpl)source;
        }
        var rv = new EventRepeatWeeklyDTOImpl();
        rv.daysOfWeek = source.daysOfWeek();
        rv.interval = source.interval();
        rv.endDate = source.endDate();
        rv.timeZone = source.timeZone();
        return rv;
    }
    public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl implements EventRepeatWeeklyDTO.Builder {
        public List<DayOfWeek> daysOfWeek;
        public short interval;
        public LocalDate endDate;
        public ZoneId timeZone;

        public EventRepeatWeeklyDTO.Builder daysOfWeek(List<DayOfWeek> daysOfWeek) {
            this.daysOfWeek = daysOfWeek;
            return this;
        }
        public EventRepeatWeeklyDTO.Builder interval(short interval) {
            this.interval = interval;
            return this;
        }
        public EventRepeatWeeklyDTO.Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        public EventRepeatWeeklyDTO.Builder timeZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public at.bestsolution.quti.service.dto.EventRepeatWeeklyDTO build() {
            var rv = new EventRepeatWeeklyDTOImpl();
            rv.daysOfWeek = daysOfWeek;
            rv.interval = interval;
            rv.endDate = endDate;
            rv.timeZone = timeZone;
            return rv;
        }
    }

    public static EventRepeatWeeklyDTO.Builder builder() {
        return new BuilderImpl();
    }
}