package at.bestsolution.quti.service.jpa.calendar;

import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.model.CalendarEntity;
import at.bestsolution.quti.rest.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.service.CalendarService;
import at.bestsolution.quti.service.DTOBuilderFactory;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CreateHandlerJPA extends BaseHandler implements CalendarService.CreateHandler {

	@Inject
	public CreateHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<String> create(DTOBuilderFactory factory, CalendarNewDTOImpl calendar) {
		Objects.requireNonNull(calendar.name(), "name must not be null");

		CalendarEntity c = new CalendarEntity();
		c.name = calendar.name();
		c.owner = calendar.owner();
		c.key = UUID.randomUUID();
		em().persist(c);
		return Result.ok(c.key.toString());
	}
}
