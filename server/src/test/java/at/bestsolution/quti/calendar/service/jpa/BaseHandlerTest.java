package at.bestsolution.quti.calendar.service.jpa;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import at.bestsolution.quti.calendar.BaseTest;
import at.bestsolution.quti.calendar.rest.RestBuilderFactory;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public abstract class BaseHandlerTest<T extends BaseHandler> extends BaseTest {
	public final T handler;

	@Inject
	public RestBuilderFactory builderFactory;

	public BaseHandlerTest(T handler) {
		this.handler = handler;
	}

	@Test
	public void testReadonly() {
		var types = new ArrayList<Class<?>>();
		Class<?> clazz = this.handler.getClass();
		do {
			types.add(clazz);
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class);

		var hasTransaction = types.stream()
				.flatMap(c -> Stream.of(c.getDeclaredMethods()))
				.flatMap(m -> Stream.of(m.getAnnotations()))
				.anyMatch(a -> a.annotationType() == Transactional.class);

		if (!hasTransaction) {
			if (!this.handler.em().unwrap(Session.class).isDefaultReadOnly()) {
				fail("Entity manager has to be marked readonly");
			}
		}
	}
}
