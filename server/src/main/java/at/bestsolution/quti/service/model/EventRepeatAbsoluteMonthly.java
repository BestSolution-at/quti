// Generated by RSD - Do not modify
package at.bestsolution.quti.service.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

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

	public interface Patch extends _Base.BaseData, EventRepeatAbsoluteMonthly {
		public Optional<Short> dayOfMonth();

		public Optional<Short> interval();

		public _Base.Nillable<LocalDate> endDate();

		public Optional<ZoneId> timeZone();

	}

	public interface PatchBuilder extends _Base.BaseDataBuilder<EventRepeatAbsoluteMonthly.Patch> {
		public PatchBuilder dayOfMonth(short dayOfMonth);

		public PatchBuilder interval(short interval);

		public PatchBuilder endDate(LocalDate endDate);

		public PatchBuilder timeZone(ZoneId timeZone);

	}
}