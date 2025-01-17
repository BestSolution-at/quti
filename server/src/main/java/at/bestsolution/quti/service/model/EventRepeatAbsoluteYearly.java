// Generated by RSD - Do not modify
package at.bestsolution.quti.service.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import at.bestsolution.quti.service.model.mixins.EventRepeatDataMixin;

public interface EventRepeatAbsoluteYearly {

	public interface Data extends _Base.BaseData, EventRepeatAbsoluteYearly, EventRepeatDataMixin, EventRepeat.Data {
		public short dayOfMonth();

		public Month month();

		public short interval();

		public LocalDate endDate();

		public ZoneId timeZone();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<EventRepeatAbsoluteYearly.Data>, EventRepeat.DataBuilder {
		public DataBuilder dayOfMonth(short dayOfMonth);

		public DataBuilder month(Month month);

		public DataBuilder interval(short interval);

		public DataBuilder endDate(LocalDate endDate);

		public DataBuilder timeZone(ZoneId timeZone);

	}

	public interface Patch extends EventRepeatAbsoluteYearly {
	}

	public interface PatchBuilder {
	}
}