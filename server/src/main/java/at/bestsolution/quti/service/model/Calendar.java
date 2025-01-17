// Generated by RSD - Do not modify
package at.bestsolution.quti.service.model;

import at.bestsolution.quti.service.model.mixins.CalendarDataMixin;

public interface Calendar {

	public interface Data extends _Base.BaseData, Calendar, CalendarDataMixin {
		public String key();

		public String name();

		public String owner();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<Calendar.Data> {
		public DataBuilder key(String key);

		public DataBuilder name(String name);

		public DataBuilder owner(String owner);

	}

	public interface Patch extends Calendar {
	}

	public interface PatchBuilder {
	}
}