// Generated by RSD - Do not modify
package at.bestsolution.quti.rest.dto;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import at.bestsolution.quti.service.dto.EventRepeatAbsoluteYearlyDTO;

public class EventRepeatAbsoluteYearlyDTOImpl extends EventRepeatDTOImpl implements EventRepeatAbsoluteYearlyDTO {
	public short dayOfMonth;
	public Month month;

	public short dayOfMonth() {
		return this.dayOfMonth;
	}

	public Month month() {
		return this.month;
	}

	public static EventRepeatAbsoluteYearlyDTOImpl of(EventRepeatAbsoluteYearlyDTO source) {
		if (source == null) {
			return null;
		} else if (source instanceof EventRepeatAbsoluteYearlyDTOImpl) {
			return (EventRepeatAbsoluteYearlyDTOImpl) source;
		}
		var rv = new EventRepeatAbsoluteYearlyDTOImpl();
		rv.dayOfMonth = source.dayOfMonth();
		rv.month = source.month();
		rv.interval = source.interval();
		rv.endDate = source.endDate();
		rv.timeZone = source.timeZone();
		return rv;
	}

	public static class BuilderImpl extends EventRepeatDTOImpl.BuilderImpl
			implements EventRepeatAbsoluteYearlyDTO.Builder {
		public short dayOfMonth;
		public Month month;
		public short interval;
		public LocalDate endDate;
		public ZoneId timeZone;

		public EventRepeatAbsoluteYearlyDTO.Builder dayOfMonth(short dayOfMonth) {
			this.dayOfMonth = dayOfMonth;
			return this;
		}

		public EventRepeatAbsoluteYearlyDTO.Builder month(Month month) {
			this.month = month;
			return this;
		}

		public EventRepeatAbsoluteYearlyDTO.Builder interval(short interval) {
			this.interval = interval;
			return this;
		}

		public EventRepeatAbsoluteYearlyDTO.Builder endDate(LocalDate endDate) {
			this.endDate = endDate;
			return this;
		}

		public EventRepeatAbsoluteYearlyDTO.Builder timeZone(ZoneId timeZone) {
			this.timeZone = timeZone;
			return this;
		}

		public at.bestsolution.quti.service.dto.EventRepeatAbsoluteYearlyDTO build() {
			var rv = new EventRepeatAbsoluteYearlyDTOImpl();
			rv.dayOfMonth = dayOfMonth;
			rv.month = month;
			rv.interval = interval;
			rv.endDate = endDate;
			rv.timeZone = timeZone;
			return rv;
		}
	}

	public static EventRepeatAbsoluteYearlyDTO.Builder builder() {
		return new BuilderImpl();
	}
}
