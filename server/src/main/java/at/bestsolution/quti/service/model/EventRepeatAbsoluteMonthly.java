// Generated by RSD - Do not modify
package at.bestsolution.quti.service.model;

import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.service.model.mixins.EventRepeatDataMixin;

public interface EventRepeatAbsoluteMonthly {

	public interface Data extends _Base.BaseData, EventRepeatAbsoluteMonthly, EventRepeatDataMixin, EventRepeat.Data {
		public short dayOfMonth();

		public short interval();

		public LocalDate endDate();

		public ZoneId timeZone();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<EventRepeatAbsoluteMonthly.Data>, EventRepeat.DataBuilder {
		public DataBuilder dayOfMonth(short dayOfMonth);

		public DataBuilder interval(short interval);

		public DataBuilder endDate(LocalDate endDate);

		public DataBuilder timeZone(ZoneId timeZone);

	}

	public interface Patch extends EventRepeatAbsoluteMonthly {
	}

	public interface PatchBuilder {
	}
}