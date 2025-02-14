package at.bestsolution.quti.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventNew;
import at.bestsolution.quti.service.model.Event.Patch;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class EventServiceImpl implements EventService {

	private final GetHandler getHandler;
	private final CreateHandler createHandler;
	private final DeleteHandler deleteHandler;
	private final MoveHandler moveHandler;
	private final CancelHandler cancelHandler;
	private final UncancelHandler uncancelHandler;
	private final EndRepeatingHandler endRepeatHandler;
	private final SetDescriptionHandler setDescriptionHandler;
	private final UpdateHandler updateHandler;

	@Inject
	public EventServiceImpl(
			GetHandler getHandler,
			CreateHandler createHandler,
			DeleteHandler deleteHandler,
			MoveHandler moveHandler,
			CancelHandler cancelHandler,
			UncancelHandler uncancelHandler,
			EndRepeatingHandler endRepeatHandler,
			SetDescriptionHandler setDescriptionHandler,
			UpdateHandler updateHandler) {
		this.getHandler = getHandler;
		this.createHandler = createHandler;
		this.deleteHandler = deleteHandler;
		this.moveHandler = moveHandler;
		this.cancelHandler = cancelHandler;
		this.uncancelHandler = uncancelHandler;
		this.endRepeatHandler = endRepeatHandler;
		this.setDescriptionHandler = setDescriptionHandler;
		this.updateHandler = updateHandler;
	}

	@Override
	public Event.Data get(BuilderFactory builderFactory, String calendarKey, String eventKey, ZoneId zone) {
		return getHandler.get(builderFactory, calendarKey, eventKey, zone);
	}

	@Override
	public String create(BuilderFactory builderFactory, String calendarKey, EventNew.Data event) {
		return createHandler.create(builderFactory, calendarKey, event);
	}

	@Override
	public void delete(BuilderFactory builderFactory, String calendarKey, String eventKey) {
		deleteHandler.delete(builderFactory, calendarKey, eventKey);
	}

	@Override
	public void endRepeat(BuilderFactory builderFactory, String calendarKey, String eventKey,
			LocalDate endDate) {
		endRepeatHandler.endRepeat(builderFactory, calendarKey, eventKey, endDate);
	}

	@Override
	public void move(BuilderFactory builderFactory, String calendarKey, String eventKey, ZonedDateTime start,
			ZonedDateTime end) {
		moveHandler.move(builderFactory, calendarKey, eventKey, start, end);
	}

	@Override
	public void cancel(BuilderFactory builderFactory, String calendarKey, String eventKey) {
		cancelHandler.cancel(builderFactory, calendarKey, eventKey);
	}

	@Override
	public void uncancel(BuilderFactory builderFactory, String calendarKey, String eventKey) {
		uncancelHandler.uncancel(builderFactory, calendarKey, eventKey);
	}

	@Override
	public void description(BuilderFactory builderFactory, String calendarKey, String eventKey,
			String description) {
		setDescriptionHandler.setDescription(builderFactory, calendarKey, eventKey, description);
	}

	@Override
	public void update(BuilderFactory factory, String calendarKey, String eventKey, Patch patch) {
		updateHandler.update(factory, calendarKey, eventKey, patch);
	}
}
