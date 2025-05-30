// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.client.jdkhttp;

import java.net.http.HttpClient;
import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;

import at.bestsolution.quti.calendar.client.BaseService;
import at.bestsolution.quti.calendar.client.CalendarService;
import at.bestsolution.quti.calendar.client.EventService;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.CalendarServiceImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.EventServiceImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.CalendarDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.CalendarDataPatchImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.CalendarNewDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.DateTimeRangeDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventDataPatchImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventNewDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatAbsoluteMonthlyDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatAbsoluteMonthlyDataPatchImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatAbsoluteYearlyDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatAbsoluteYearlyDataPatchImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatDailyDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatDailyDataPatchImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatRelativeMonthlyDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatRelativeMonthlyDataPatchImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatRelativeYearlyDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatRelativeYearlyDataPatchImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatWeeklyDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventRepeatWeeklyDataPatchImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventSearchDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventViewFilterDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.SeriesEventViewDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.SeriesMovedEventViewDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.SingleEventViewDataImpl;
import at.bestsolution.quti.calendar.client.model._Base;
import at.bestsolution.quti.calendar.client.model.Calendar;
import at.bestsolution.quti.calendar.client.model.CalendarNew;
import at.bestsolution.quti.calendar.client.model.DateTimeRange;
import at.bestsolution.quti.calendar.client.model.Event;
import at.bestsolution.quti.calendar.client.model.EventNew;
import at.bestsolution.quti.calendar.client.model.EventRepeatAbsoluteMonthly;
import at.bestsolution.quti.calendar.client.model.EventRepeatAbsoluteYearly;
import at.bestsolution.quti.calendar.client.model.EventRepeatDaily;
import at.bestsolution.quti.calendar.client.model.EventRepeatRelativeMonthly;
import at.bestsolution.quti.calendar.client.model.EventRepeatRelativeYearly;
import at.bestsolution.quti.calendar.client.model.EventRepeatWeekly;
import at.bestsolution.quti.calendar.client.model.EventSearch;
import at.bestsolution.quti.calendar.client.model.EventViewFilter;
import at.bestsolution.quti.calendar.client.model.SeriesEventView;
import at.bestsolution.quti.calendar.client.model.SeriesMovedEventView;
import at.bestsolution.quti.calendar.client.model.SingleEventView;
import at.bestsolution.quti.calendar.client.QutiClient;

public class JDKQutiClient implements QutiClient {
	private static Map<Class<?>, Supplier<Object>> BUILDER_CREATOR_MAP = new HashMap<>();
	private static Map<Class<?>, BiFunction<HttpClient, String, Object>> SERVICE_CREATOR_MAP = new HashMap<>();

	static {
		registerBuilderCreator(Calendar.DataBuilder.class, CalendarDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(CalendarNew.DataBuilder.class, CalendarNewDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventRepeatDaily.DataBuilder.class, EventRepeatDailyDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventRepeatWeekly.DataBuilder.class, EventRepeatWeeklyDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventRepeatAbsoluteMonthly.DataBuilder.class, EventRepeatAbsoluteMonthlyDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventRepeatAbsoluteYearly.DataBuilder.class, EventRepeatAbsoluteYearlyDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventRepeatRelativeMonthly.DataBuilder.class, EventRepeatRelativeMonthlyDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventRepeatRelativeYearly.DataBuilder.class, EventRepeatRelativeYearlyDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventNew.DataBuilder.class, EventNewDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(Event.DataBuilder.class, EventDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(SingleEventView.DataBuilder.class, SingleEventViewDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(SeriesMovedEventView.DataBuilder.class, SeriesMovedEventViewDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(SeriesEventView.DataBuilder.class, SeriesEventViewDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventViewFilter.DataBuilder.class, EventViewFilterDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(EventSearch.DataBuilder.class, EventSearchDataImpl.DataBuilderImpl::new);
		registerBuilderCreator(DateTimeRange.DataBuilder.class, DateTimeRangeDataImpl.DataBuilderImpl::new);

		registerBuilderCreator(Calendar.PatchBuilder.class, CalendarDataPatchImpl.PatchBuilderImpl::new);
		registerBuilderCreator(EventRepeatDaily.PatchBuilder.class, EventRepeatDailyDataPatchImpl.PatchBuilderImpl::new);
		registerBuilderCreator(EventRepeatWeekly.PatchBuilder.class, EventRepeatWeeklyDataPatchImpl.PatchBuilderImpl::new);
		registerBuilderCreator(EventRepeatAbsoluteMonthly.PatchBuilder.class, EventRepeatAbsoluteMonthlyDataPatchImpl.PatchBuilderImpl::new);
		registerBuilderCreator(EventRepeatAbsoluteYearly.PatchBuilder.class, EventRepeatAbsoluteYearlyDataPatchImpl.PatchBuilderImpl::new);
		registerBuilderCreator(EventRepeatRelativeMonthly.PatchBuilder.class, EventRepeatRelativeMonthlyDataPatchImpl.PatchBuilderImpl::new);
		registerBuilderCreator(EventRepeatRelativeYearly.PatchBuilder.class, EventRepeatRelativeYearlyDataPatchImpl.PatchBuilderImpl::new);
		registerBuilderCreator(Event.PatchBuilder.class, EventDataPatchImpl.PatchBuilderImpl::new);

		registerServiceCreator(CalendarService.class, CalendarServiceImpl::new);
		registerServiceCreator(EventService.class, EventServiceImpl::new);
	}

	private static void registerBuilderCreator(Class<?> clazz, Supplier<Object> constructor) {
		BUILDER_CREATOR_MAP.put(clazz, constructor);
	}

	private static void registerServiceCreator(Class<?> clazz, BiFunction<HttpClient, String, Object> constructor) {
		SERVICE_CREATOR_MAP.put(clazz, constructor);
	}

	private final URI baseURI;
	private final HttpClient httpClient;

	JDKQutiClient(URI baseURI) {
		this.baseURI = baseURI;
		this.httpClient = HttpClient.newHttpClient();
	}

	public static QutiClient create(URI baseURI) {
		return new JDKQutiClient(baseURI);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends _Base.BaseDataBuilder<?>> T builder(Class<T> clazz) {
		var builderConstructor = BUILDER_CREATOR_MAP.get(clazz);
		if (builderConstructor != null) {
			return (T) builderConstructor.get();
		}
		throw new IllegalArgumentException(String.format("Unsupported build '%s'", clazz));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseService> T service(Class<T> clazz) {
		var serviceConstructor = SERVICE_CREATOR_MAP.get(clazz);
		if (serviceConstructor != null) {
			return (T) serviceConstructor.apply(this.httpClient, this.baseURI.toString());
		}
		throw new IllegalArgumentException(String.format("Unsupported service '%s'", clazz));
	}
}
