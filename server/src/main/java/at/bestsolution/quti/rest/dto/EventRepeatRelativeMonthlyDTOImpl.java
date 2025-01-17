// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.service.dto.EventRepeatRelativeMonthlyDTO;

public class EventRepeatRelativeMonthlyDTOImpl extends EventRepeatDTOImpl implements EventRepeatRelativeMonthlyDTO {
	public List<DayOfWeek> daysOfWeek;

	public List<DayOfWeek> daysOfWeek() {
		return this.daysOfWeek;
	}

	public static EventRepeatRelativeMonthlyDTOImpl of(EventRepeatRelativeMonthlyDTO source) {
		if (source == null) {
			return null;
		} else if (source instanceof EventRepeatRelativeMonthlyDTOImpl) {
			return (EventRepeatRelativeMonthlyDTOImpl) source;
		}
		var rv = new EventRepeatRelativeMonthlyDTOImpl();
		rv.daysOfWeek = source.daysOfWeek();
		rv.interval = source.interval();
		rv.endDate = source.endDate();
		rv.timeZone = source.timeZone();
		return rv;
	}

	public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl
			implements EventRepeatRelativeMonthlyDTO.Builder {
		public List<DayOfWeek> daysOfWeek;
		public short interval;
		public LocalDate endDate;
		public ZoneId timeZone;

		public EventRepeatRelativeMonthlyDTO.Builder daysOfWeek(List<DayOfWeek> daysOfWeek) {
			this.daysOfWeek = daysOfWeek;
			return this;
		}

		public EventRepeatRelativeMonthlyDTO.Builder interval(short interval) {
			this.interval = interval;
			return this;
		}

		public EventRepeatRelativeMonthlyDTO.Builder endDate(LocalDate endDate) {
			this.endDate = endDate;
			return this;
		}

		public EventRepeatRelativeMonthlyDTO.Builder timeZone(ZoneId timeZone) {
			this.timeZone = timeZone;
			return this;
		}

		public at.bestsolution.quti.service.dto.EventRepeatRelativeMonthlyDTO build() {
			var rv = new EventRepeatRelativeMonthlyDTOImpl();
			rv.daysOfWeek = daysOfWeek;
			rv.interval = interval;
			rv.endDate = endDate;
			rv.timeZone = timeZone;
			return rv;
		}
	}

	public static EventRepeatRelativeMonthlyDTO.Builder builder() {
		return new BuilderImpl();
	}
}
