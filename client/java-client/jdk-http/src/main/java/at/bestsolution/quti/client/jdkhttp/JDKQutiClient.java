// Generated by RSD - Do not modify
package at.bestsolution.quti.client.jdkhttp;

import java.net.http.HttpClient;
import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;

import at.bestsolution.quti.client.BaseService;
import at.bestsolution.quti.client.CalendarService;
import at.bestsolution.quti.client.dto.BaseDTO;
import at.bestsolution.quti.client.dto.CalendarDTO;
import at.bestsolution.quti.client.dto.CalendarNewDTO;
import at.bestsolution.quti.client.dto.EventDTO;
import at.bestsolution.quti.client.dto.EventNewDTO;
import at.bestsolution.quti.client.dto.EventRepeatDTO;
import at.bestsolution.quti.client.dto.EventViewDTO;
import at.bestsolution.quti.client.dto.EventViewFilterDTO;
import at.bestsolution.quti.client.EventService;
import at.bestsolution.quti.client.jdkhttp.impl.CalendarServiceImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.CalendarDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventNewDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventRepeatDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventViewDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventViewFilterDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.EventServiceImpl;
import at.bestsolution.quti.client.QutiClient;

public class JDKQutiClient implements QutiClient {
    private static Map<Class<?>, Supplier<Object>> BUILDER_CREATOR_MAP = new HashMap<>();
    private static Map<Class<?>, BiFunction<HttpClient, String, Object>> SERVICE_CREATOR_MAP = new HashMap<>();

    static {
        registerBuilderCreator(CalendarDTO.Builder.class, CalendarDTOImpl.BuilderImpl::new);
        registerBuilderCreator(CalendarNewDTO.Builder.class, CalendarNewDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventNewDTO.Builder.class, EventNewDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventDTO.Builder.class, EventDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventViewFilterDTO.Builder.class, EventViewFilterDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventRepeatDTO.EventRepeatDailyDTO.Builder.class, EventRepeatDTOImpl.EventRepeatDailyDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventRepeatDTO.EventRepeatWeeklyDTO.Builder.class, EventRepeatDTOImpl.EventRepeatWeeklyDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventRepeatDTO.EventRepeatAbsoluteMonthlyDTO.Builder.class, EventRepeatDTOImpl.EventRepeatAbsoluteMonthlyDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventRepeatDTO.EventRepeatAbsoluteYearlyDTO.Builder.class, EventRepeatDTOImpl.EventRepeatAbsoluteYearlyDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventRepeatDTO.EventRepeatRelativeMonthlyDTO.Builder.class, EventRepeatDTOImpl.EventRepeatRelativeMonthlyDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventRepeatDTO.EventRepeatRelativeYearlyDTO.Builder.class, EventRepeatDTOImpl.EventRepeatRelativeYearlyDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventViewDTO.SingleEventViewDTO.Builder.class, EventViewDTOImpl.SingleEventViewDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventViewDTO.SeriesMovedEventViewDTO.Builder.class, EventViewDTOImpl.SeriesMovedEventViewDTOImpl.BuilderImpl::new);
        registerBuilderCreator(EventViewDTO.SeriesEventViewDTO.Builder.class, EventViewDTOImpl.SeriesEventViewDTOImpl.BuilderImpl::new);

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
    public <T extends BaseDTO.Builder> T builder(Class<T> clazz) {
        var builderConstructor = BUILDER_CREATOR_MAP.get(clazz);
        if( builderConstructor != null ) {
            return (T)builderConstructor.get();
        }
        throw new IllegalArgumentException(String.format("Unsupported build '%s'", clazz));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseService> T service(Class<T> clazz) {
        var serviceConstructor = SERVICE_CREATOR_MAP.get(clazz);
        if( serviceConstructor != null ) {
            return (T) serviceConstructor.apply(this.httpClient, this.baseURI.toString());
        }
        throw new IllegalArgumentException(String.format("Unsupported service '%s'", clazz));
    }}