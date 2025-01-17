// Generated by RSD - Do not modify
package at.bestsolution.quti.rest;

import jakarta.inject.Singleton;
import jakarta.json.JsonObject;
import at.bestsolution.quti.rest.model.CalendarDataImpl;
import at.bestsolution.quti.rest.model.CalendarNewDataImpl;
import at.bestsolution.quti.rest.model.EventDataImpl;
import at.bestsolution.quti.rest.model.EventNewDataImpl;
import at.bestsolution.quti.rest.model.EventRepeatAbsoluteMonthlyDataImpl;
import at.bestsolution.quti.rest.model.EventRepeatAbsoluteYearlyDataImpl;
import at.bestsolution.quti.rest.model.EventRepeatDailyDataImpl;
import at.bestsolution.quti.rest.model.EventRepeatRelativeMonthlyDataImpl;
import at.bestsolution.quti.rest.model.EventRepeatRelativeYearlyDataImpl;
import at.bestsolution.quti.rest.model.EventRepeatWeeklyDataImpl;
import at.bestsolution.quti.rest.model.EventViewFilterDataImpl;
import at.bestsolution.quti.rest.model.SeriesEventViewDataImpl;
import at.bestsolution.quti.rest.model.SeriesMovedEventViewDataImpl;
import at.bestsolution.quti.rest.model.SingleEventViewDataImpl;
import at.bestsolution.quti.rest.model._JsonUtils;
import at.bestsolution.quti.service.model.Calendar;
import at.bestsolution.quti.service.model.CalendarNew;
import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventNew;
import at.bestsolution.quti.service.model.EventRepeatAbsoluteMonthly;
import at.bestsolution.quti.service.model.EventRepeatAbsoluteYearly;
import at.bestsolution.quti.service.model.EventRepeatDaily;
import at.bestsolution.quti.service.model.EventRepeatRelativeMonthly;
import at.bestsolution.quti.service.model.EventRepeatRelativeYearly;
import at.bestsolution.quti.service.model.EventRepeatWeekly;
import at.bestsolution.quti.service.model.EventViewFilter;
import at.bestsolution.quti.service.model.SeriesEventView;
import at.bestsolution.quti.service.model.SeriesMovedEventView;
import at.bestsolution.quti.service.model.SingleEventView;
import at.bestsolution.quti.service.model._Base;
import at.bestsolution.quti.service.DataBuilderFactory;

@Singleton
public class RestDTOBuilderFactory implements DataBuilderFactory {
	@Override
	public <T extends _Base.BaseDataBuilder<?>> T builder(Class<T> type) {
		if (type == Calendar.DataBuilder.class) {
			return type.cast(CalendarDataImpl.builder());
		}
		if (type == CalendarNew.DataBuilder.class) {
			return type.cast(CalendarNewDataImpl.builder());
		}
		if (type == EventNew.DataBuilder.class) {
			return type.cast(EventNewDataImpl.builder());
		}
		if (type == Event.DataBuilder.class) {
			return type.cast(EventDataImpl.builder());
		}
		if (type == EventViewFilter.DataBuilder.class) {
			return type.cast(EventViewFilterDataImpl.builder());
		}
		if (type == EventRepeatDaily.DataBuilder.class) {
			return type.cast(EventRepeatDailyDataImpl.builder());
		}
		if (type == EventRepeatWeekly.DataBuilder.class) {
			return type.cast(EventRepeatWeeklyDataImpl.builder());
		}
		if (type == EventRepeatAbsoluteMonthly.DataBuilder.class) {
			return type.cast(EventRepeatAbsoluteMonthlyDataImpl.builder());
		}
		if (type == EventRepeatAbsoluteYearly.DataBuilder.class) {
			return type.cast(EventRepeatAbsoluteYearlyDataImpl.builder());
		}
		if (type == EventRepeatRelativeMonthly.DataBuilder.class) {
			return type.cast(EventRepeatRelativeMonthlyDataImpl.builder());
		}
		if (type == EventRepeatRelativeYearly.DataBuilder.class) {
			return type.cast(EventRepeatRelativeYearlyDataImpl.builder());
		}
		if (type == SingleEventView.DataBuilder.class) {
			return type.cast(SingleEventViewDataImpl.builder());
		}
		if (type == SeriesMovedEventView.DataBuilder.class) {
			return type.cast(SeriesMovedEventViewDataImpl.builder());
		}
		if (type == SeriesEventView.DataBuilder.class) {
			return type.cast(SeriesEventViewDataImpl.builder());
		}
		throw new IllegalArgumentException("Unsupported Builder '%s'".formatted(type));
	}

	public <T extends _Base.BaseData> T of(Class<T> type, String data) {
		if (type == Calendar.Data.class) {
			return type.cast(_JsonUtils.fromString(data, CalendarDataImpl::of));
		}
		if (type == CalendarNew.Data.class) {
			return type.cast(_JsonUtils.fromString(data, CalendarNewDataImpl::of));
		}
		if (type == EventNew.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventNewDataImpl::of));
		}
		if (type == Event.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventDataImpl::of));
		}
		if (type == EventViewFilter.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventViewFilterDataImpl::of));
		}
		if (type == EventRepeatDaily.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventRepeatDailyDataImpl::of));
		}
		if (type == EventRepeatWeekly.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventRepeatWeeklyDataImpl::of));
		}
		if (type == EventRepeatAbsoluteMonthly.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventRepeatAbsoluteMonthlyDataImpl::of));
		}
		if (type == EventRepeatAbsoluteYearly.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventRepeatAbsoluteYearlyDataImpl::of));
		}
		if (type == EventRepeatRelativeMonthly.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventRepeatRelativeMonthlyDataImpl::of));
		}
		if (type == EventRepeatRelativeYearly.Data.class) {
			return type.cast(_JsonUtils.fromString(data, EventRepeatRelativeYearlyDataImpl::of));
		}
		if (type == SingleEventView.Data.class) {
			return type.cast(_JsonUtils.fromString(data, SingleEventViewDataImpl::of));
		}
		if (type == SeriesMovedEventView.Data.class) {
			return type.cast(_JsonUtils.fromString(data, SeriesMovedEventViewDataImpl::of));
		}
		if (type == SeriesEventView.Data.class) {
			return type.cast(_JsonUtils.fromString(data, SeriesEventViewDataImpl::of));
		}
		throw new IllegalArgumentException("Unsupported Builder '%s'".formatted(type));
	}
}
