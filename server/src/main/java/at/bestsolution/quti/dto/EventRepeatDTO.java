package at.bestsolution.quti.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
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
public abstract class EventRepeatDTO {
	public short interval;
	public LocalDate endDate;
	public String timeZone;

	public static class EventRepeatDailyDTO extends EventRepeatDTO {
		// nothing
	}

	public static class EventRepeatWeeklyDTO extends EventRepeatDTO {
		public List<DayOfWeek> daysOfWeek;
	}

	public static class EventRepeatAbsoluteMonthlyDTO extends EventRepeatDTO {
		public short dayOfMonth;
	}

	public static class EventRepeatAbsoluteYearlyDTO extends EventRepeatDTO {
		public short dayOfMonth;
		public Month month;
	}

	public static class EventRepeatRelativeMonthlyDTO extends EventRepeatDTO {
		public List<DayOfWeek> daysOfWeek;
	}

	public static class EventRepeatRelativeYearlyDTO extends EventRepeatDTO {
		public List<DayOfWeek> daysOfWeek;
		public Month month;
	}
}
