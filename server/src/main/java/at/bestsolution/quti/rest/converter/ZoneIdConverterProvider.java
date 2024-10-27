package at.bestsolution.quti.rest.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.ZoneId;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ZoneIdConverterProvider implements ParamConverterProvider {

	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if( rawType == ZoneId.class ) {
			return (ParamConverter<T>) new ZoneIdConverter();
		}
		return null;
	}

	static class ZoneIdConverter implements ParamConverter<ZoneId> {

		@Override
		public ZoneId fromString(String value) {
			return ZoneId.of(value);
		}

		@Override
		public String toString(ZoneId value) {
			return value.toString();
		}

	}
}
