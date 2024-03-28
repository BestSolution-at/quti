package at.bestsolution.quti.client.jdkhttp;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import at.bestsolution.quti.client.BaseService;
import at.bestsolution.quti.client.CalendarService;
import at.bestsolution.quti.client.EventService;
import at.bestsolution.quti.client.QutiClient;
import at.bestsolution.quti.client.dto.CalendarNewDTO;
import at.bestsolution.quti.client.dto.EventNewDTO;
import at.bestsolution.quti.client.dto.BaseDTO.Builder;
import at.bestsolution.quti.client.jdkhttp.impl.CalendarServiceImpl;
import at.bestsolution.quti.client.jdkhttp.impl.EventServiceImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventNewDTOImpl;

public class JDKQutiClient implements QutiClient {
    private static Map<Class<?>,Supplier<Object>> BUILDER_CREATOR_MAP = new HashMap<>();
    private static Map<Class<?>, BiFunction<HttpClient, String, Object>> SERVICE_CREATOR_MAP = new HashMap<>();

    static {
		registerBuilderCreator(EventNewDTO.Builder.class, EventNewDTOImpl.BuilderImpl::new);
        registerBuilderCreator(CalendarNewDTO.Builder.class, CalendarNewDTOImpl.BuilderImpl::new);

        registerServiceCreator(CalendarService.class, CalendarServiceImpl::new);
        registerServiceCreator(EventService.class, EventServiceImpl::new);
	}

    private static void registerBuilderCreator(Class<?> clazz, Supplier<Object> constructor) {
		BUILDER_CREATOR_MAP.put(clazz, constructor);
	}

    private static void registerServiceCreator(Class<?> clazz, BiFunction<HttpClient, String, Object> constructor) {
        SERVICE_CREATOR_MAP.put(clazz, constructor);
    }

    public final URI baseURI;
    private final HttpClient httpClient;
    
    JDKQutiClient(URI baseURI) {
        this.baseURI = baseURI;
        this.httpClient = HttpClient.newHttpClient();
    }

    public static QutiClient create(URI baseURI) {
        return new JDKQutiClient(baseURI);
    }

    @Override
    public <T extends Builder> T builder(Class<T> clazz) {
        var builderConstructor = BUILDER_CREATOR_MAP.get(clazz);
		if( builderConstructor != null ) {
			return (T)builderConstructor.get();
		}
		throw new IllegalArgumentException(String.format("Unsupported build '%s'", clazz));
    }

    @Override
    public <T extends BaseService> T service(Class<T> clazz) {
        var serviceConstructor = SERVICE_CREATOR_MAP.get(clazz);
        if( serviceConstructor != null ) {
            return (T) serviceConstructor.apply(this.httpClient, this.baseURI.toString());
        }
        throw new IllegalArgumentException(String.format("Unsupported service '%s'", clazz));
    }
}
