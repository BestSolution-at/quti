package at.bestsolution.quti.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
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
	public Result<Event.Data> get(BuilderFactory builderFactory, String calendarKey, String eventKey, ZoneId zone) {
		return getHandler.get(builderFactory, calendarKey, eventKey, zone);
	}

	@Override
	public Result<String> create(BuilderFactory builderFactory, String calendarKey, EventNew.Data event) {
		return createHandler.create(builderFactory, calendarKey, event);
	}

	@Override
	public Result<Void> delete(BuilderFactory builderFactory, String calendarKey, String eventKey) {
		return deleteHandler.delete(builderFactory, calendarKey, eventKey);
	}

	@Override
	public Result<Void> endRepeat(BuilderFactory builderFactory, String calendarKey, String eventKey,
			LocalDate endDate) {
		return endRepeatHandler.endRepeat(builderFactory, calendarKey, eventKey, endDate);
	}

	@Override
	public Result<Void> move(BuilderFactory builderFactory, String calendarKey, String eventKey, ZonedDateTime start,
			ZonedDateTime end) {
		return moveHandler.move(builderFactory, calendarKey, eventKey, start, end);
	}

	@Override
	public Result<Void> cancel(BuilderFactory builderFactory, String calendarKey, String eventKey) {
		return cancelHandler.cancel(builderFactory, calendarKey, eventKey);
	}

	@Override
	public Result<Void> uncancel(BuilderFactory builderFactory, String calendarKey, String eventKey) {
		return uncancelHandler.uncancel(builderFactory, calendarKey, eventKey);
	}

	@Override
	public Result<Void> description(BuilderFactory builderFactory, String calendarKey, String eventKey,
			String description) {
		return setDescriptionHandler.setDescription(builderFactory, calendarKey, eventKey, description);
	}

	@Override
	public Result<Void> update(BuilderFactory factory, String calendarKey, String eventKey, Patch patch) {
		return updateHandler.update(factory, calendarKey, eventKey, patch);
	}
}
