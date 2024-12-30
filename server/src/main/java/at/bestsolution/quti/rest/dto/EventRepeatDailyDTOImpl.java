// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.service.dto.EventRepeatDailyDTO;

public class EventRepeatDailyDTOImpl extends EventRepeatDTOImpl implements EventRepeatDailyDTO {

    public static EventRepeatDailyDTOImpl of(EventRepeatDailyDTO source) {
        if(source == null) {
            return null;
        }
        else if(source instanceof EventRepeatDailyDTOImpl) {
            return (EventRepeatDailyDTOImpl)source;
        }
        var rv = new EventRepeatDailyDTOImpl();
        rv.interval = source.interval();
        rv.endDate = source.endDate();
        rv.timeZone = source.timeZone();
        return rv;
    }
    public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl implements EventRepeatDailyDTO.Builder {
        public short interval;
        public LocalDate endDate;
        public ZoneId timeZone;

        public EventRepeatDailyDTO.Builder interval(short interval) {
            this.interval = interval;
            return this;
        }
        public EventRepeatDailyDTO.Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        public EventRepeatDailyDTO.Builder timeZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public at.bestsolution.quti.service.dto.EventRepeatDailyDTO build() {
            var rv = new EventRepeatDailyDTOImpl();
            rv.interval = interval;
            rv.endDate = endDate;
            rv.timeZone = timeZone;
            return rv;
        }
    }

    public static EventRepeatDailyDTO.Builder builder() {
        return new BuilderImpl();
    }
}