package at.bestsolution.quti.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.rest.RestDTOBuilderFactory;
import at.bestsolution.quti.rest.dto.EventDTOImpl;
import at.bestsolution.quti.rest.dto.EventNewDTOImpl;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class EventServiceImpl implements EventService {
	private final RestDTOBuilderFactory builderFactory;

	private final GetHandler getHandler;
	private final CreateHandler createHandler;
	private final DeleteHandler deleteHandler;
	private final MoveHandler moveHandler;
	private final CancelHandler cancelHandler;
	private final UncancelHandler uncancelHandler;
	private final EndRepeatingHandler endRepeatHandler;
	private final SetDescriptionHandler setDescriptionHandler;

	@Inject
	public EventServiceImpl(RestDTOBuilderFactory builderFactory,
		GetHandler getHandler,
		CreateHandler createHandler,
		DeleteHandler deleteHandler,
		MoveHandler moveHandler,
		CancelHandler cancelHandler,
		UncancelHandler uncancelHandler,
		EndRepeatingHandler endRepeatHandler,
		SetDescriptionHandler setDescriptionHandler) {
		this.builderFactory = builderFactory;
		this.getHandler = getHandler;
		this.createHandler = createHandler;
		this.deleteHandler = deleteHandler;
		this.moveHandler = moveHandler;
		this.cancelHandler = cancelHandler;
		this.uncancelHandler = uncancelHandler;
		this.endRepeatHandler = endRepeatHandler;
		this.setDescriptionHandler = setDescriptionHandler;
	}

	@Override
	public Result<EventDTOImpl> get(String calendarKey, String eventKey, ZoneId zone) {
		return getHandler.get(this.builderFactory, calendarKey, eventKey, zone);
	}

	@Override
	public Result<String> create(String calendarKey, EventNewDTOImpl event) {
		return createHandler.create(this.builderFactory, calendarKey, event);
	}

	@Override
	public Result<Void> delete(String calendarKey, String eventKey) {
		return deleteHandler.delete(this.builderFactory, calendarKey, eventKey);
	}

	@Override
	public Result<Void> endRepeat(String calendarKey, String eventKey, LocalDate endDate) {
		return endRepeatHandler.endRepeat(this.builderFactory, calendarKey, eventKey, endDate);
	}

	@Override
	public Result<Void> move(String calendarKey, String eventKey, ZonedDateTime start, ZonedDateTime end) {
		return moveHandler.move(this.builderFactory, calendarKey, eventKey, start, end);
	}

	@Override
	public Result<Void> cancel(String calendarKey, String eventKey) {
		return cancelHandler.cancel(this.builderFactory, calendarKey, eventKey);
	}

	@Override
	public Result<Void> uncancel(String calendarKey, String eventKey) {
		return uncancelHandler.uncancel(this.builderFactory, calendarKey, eventKey);
	}

	@Override
	public Result<Void> setDescription(String calendarKey, String eventKey, String description) {
		return setDescriptionHandler.setDescription(this.builderFactory, calendarKey, eventKey, description);
	}

}
