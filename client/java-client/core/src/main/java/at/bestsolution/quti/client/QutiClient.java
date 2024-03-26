package at.bestsolution.quti.client;

import java.net.URI;
import java.util.ServiceLoader;

import at.bestsolution.quti.client.dto.BaseDTO;
import at.bestsolution.quti.client.spi.QutiClientFactory;

public interface QutiClient {
    public static QutiClient create(URI baseURL) {
        return ServiceLoader.load(QutiClientFactory.class).iterator().next().create(baseURL);
    }

    public <T extends BaseDTO.Builder> T builder(Class<T> clazz);
    public CalendarsService calendars();
    public CalendarService calendar(String key);
}
