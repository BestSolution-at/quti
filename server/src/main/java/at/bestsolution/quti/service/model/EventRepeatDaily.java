// Generated by RSD - Do not modify
package at.bestsolution.quti.service.model;

import java.time.LocalDate;
import java.time.ZoneId;

import at.bestsolution.quti.service.model.mixins.EventRepeatDataMixin;

public interface EventRepeatDaily {

	public interface Data extends _Base.BaseData, EventRepeatDaily, EventRepeatDataMixin, EventRepeat.Data {
		public short interval();

		public LocalDate endDate();

		public ZoneId timeZone();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<EventRepeatDaily.Data>, EventRepeat.DataBuilder {
		public DataBuilder interval(short interval);

		public DataBuilder endDate(LocalDate endDate);

		public DataBuilder timeZone(ZoneId timeZone);

	}

	public interface Patch extends EventRepeatDaily {
	}

	public interface PatchBuilder {
	}
}