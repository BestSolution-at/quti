package at.bestsolution.quti.handler.calendar;

import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.handler.BaseHandler;
import at.bestsolution.quti.model.CalendarEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CreateHandler extends BaseHandler {

	@Inject
	public CreateHandler(EntityManager em) {
		super(em);
	}

	@Transactional
	public String create(String name) {
		Objects.requireNonNull(name, "name must not be null");

		CalendarEntity c = new CalendarEntity();
		c.name = name;
		c.key = UUID.randomUUID();
		em().persist(c);
		return c.key.toString();
	}
}
