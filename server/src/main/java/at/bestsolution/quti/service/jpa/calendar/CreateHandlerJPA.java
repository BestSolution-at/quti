package at.bestsolution.quti.service.jpa.calendar;

import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.impl.CalendarServiceImpl;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.service.model.CalendarNew;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CreateHandlerJPA extends BaseHandler implements CalendarServiceImpl.CreateHandler {

	@Inject
	public CreateHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public String create(BuilderFactory factory, CalendarNew.Data calendar) {
		Objects.requireNonNull(calendar.name(), "name must not be null");

		CalendarEntity c = new CalendarEntity();
		c.name = calendar.name();
		c.owner = calendar.owner();
		c.key = UUID.randomUUID();
		em().persist(c);
		return c.key.toString();
	}
}
