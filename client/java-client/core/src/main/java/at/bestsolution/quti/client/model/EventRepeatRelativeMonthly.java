// Generated by RSD - Do not modify
package at.bestsolution.quti.client.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import at.bestsolution.quti.client.model.mixins.EventRepeatDataMixin;

public interface EventRepeatRelativeMonthly {

	public interface Data extends _Base.BaseData, EventRepeatRelativeMonthly, EventRepeatDataMixin, EventRepeat.Data {
		public List<DayOfWeek> daysOfWeek();

		public short interval();

		public LocalDate endDate();

		public ZoneId timeZone();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<EventRepeatRelativeMonthly.Data>, EventRepeat.DataBuilder {
		public DataBuilder daysOfWeek(List<DayOfWeek> daysOfWeek);

		public DataBuilder interval(short interval);

		public DataBuilder endDate(LocalDate endDate);

		public DataBuilder timeZone(ZoneId timeZone);

	}

	public interface Patch extends EventRepeatRelativeMonthly {
	}

	public interface PatchBuilder {
	}
}