package at.bestsolution.quti.client.jdkhttp;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import at.bestsolution.quti.client.Calendar;
import at.bestsolution.quti.client.Calendars;
import at.bestsolution.quti.client.QutiClient;
import at.bestsolution.quti.client.dto.CalendarNewDTO;
import at.bestsolution.quti.client.dto.EventNewDTO;
import at.bestsolution.quti.client.dto.BaseDTO.Builder;
import at.bestsolution.quti.client.jdkhttp.impl.CalendarsImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventNewDTOImpl;

public class JDKQutiClient implements QutiClient {
    private static Map<Class<?>,Supplier<Object>> BUILDER_CREATOR_MAP = new HashMap<>();

    static {
		registerBuilderCreator(EventNewDTO.Builder.class, EventNewDTOImpl.BuilderImpl::new);
        registerBuilderCreator(CalendarNewDTO.Builder.class, CalendarNewDTOImpl.BuilderImpl::new);
	}

    private static void registerBuilderCreator(Class<?> clazz, Supplier<Object> constructor) {
		BUILDER_CREATOR_MAP.put(clazz, constructor);
	}

    public final URI baseURI;
    
    JDKQutiClient(URI baseURI) {
        this.baseURI = baseURI;
    }

    public static QutiClient create(URI baseURI) {
        return new JDKQutiClient(baseURI);
    }

    @Override
    public <T extends Builder<?>> T builder(Class<T> clazz) {
        var builderConstructor = BUILDER_CREATOR_MAP.get(clazz);
		if( builderConstructor != null ) {
			return (T)builderConstructor.get();
		}
		throw new IllegalArgumentException(String.format("Unsupported build '%s'", clazz));
    }

    @Override
    public Calendars calendars() {
        return new CalendarsImpl(HttpClient.newHttpClient(), baseURI.toString());
    }

    @Override
    public Calendar calendar(String key) {
        return calendars().calendar(key);
    }
}
