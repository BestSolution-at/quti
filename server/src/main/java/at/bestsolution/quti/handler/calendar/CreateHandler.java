package at.bestsolution.quti.handler.calendar;

import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.Utils.Result;
import at.bestsolution.quti.dto.CalendarNewDTO;
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
	public Result<String> create(CalendarNewDTO calendar) {
		Objects.requireNonNull(calendar.name(), "name must not be null");

		CalendarEntity c = new CalendarEntity();
		c.name = calendar.name();
		c.owner = calendar.owner();
		c.key = UUID.randomUUID();
		em().persist(c);
		return Result.ok(c.key.toString());
	}
}
