// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.service.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import at.bestsolution.quti.calendar.service.model.mixins.EventRepeatDataMixin;

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

	public interface Patch extends _Base.BaseData, EventRepeatRelativeMonthly {
		public Optional<List<DayOfWeek>> daysOfWeek();

		public Optional<Short> interval();

		public _Base.Nillable<LocalDate> endDate();

		public Optional<ZoneId> timeZone();

	}

	public interface PatchBuilder extends _Base.BaseDataBuilder<EventRepeatRelativeMonthly.Patch> {
		public PatchBuilder daysOfWeek(List<DayOfWeek> daysOfWeek);

		public PatchBuilder interval(short interval);

		public PatchBuilder endDate(LocalDate endDate);

		public PatchBuilder timeZone(ZoneId timeZone);

	}
}
