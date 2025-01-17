// Generated by RSD - Do not modify
package at.bestsolution.quti.client.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.client.model.mixins.EventRepeatDataMixin;

public interface EventRepeatRelativeYearly {

	public interface Data extends _Base.BaseData, EventRepeatRelativeYearly, EventRepeatDataMixin, EventRepeat.Data {
		public List<DayOfWeek> daysOfWeek();

		public Month month();

		public short interval();

		public LocalDate endDate();

		public ZoneId timeZone();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<EventRepeatRelativeYearly.Data>, EventRepeat.DataBuilder {
		public DataBuilder daysOfWeek(List<DayOfWeek> daysOfWeek);

		public DataBuilder month(Month month);

		public DataBuilder interval(short interval);

		public DataBuilder endDate(LocalDate endDate);

		public DataBuilder timeZone(ZoneId timeZone);

	}

	public interface Patch extends EventRepeatRelativeYearly {
	}

	public interface PatchBuilder {
	}
}