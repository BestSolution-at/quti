// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.client.model;

import at.bestsolution.quti.calendar.client.model.mixins.CalendarDataMixin;

public interface CalendarNew {

	public interface Data extends _Base.BaseData, CalendarNew, CalendarDataMixin {
		public String name();

		public String owner();

	}

	public interface DataBuilder extends _Base.BaseDataBuilder<CalendarNew.Data> {
		public DataBuilder name(String name);

		public DataBuilder owner(String owner);

	}
}
