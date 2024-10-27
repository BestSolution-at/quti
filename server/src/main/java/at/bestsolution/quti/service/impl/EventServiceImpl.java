package at.bestsolution.quti.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import at.bestsolution.quti.dto.EventDTO;
import at.bestsolution.quti.dto.EventNewDTO;
import at.bestsolution.quti.handler.event.CancelHandler;
import at.bestsolution.quti.handler.event.CreateHandler;
import at.bestsolution.quti.handler.event.DeleteHandler;
import at.bestsolution.quti.handler.event.EndRepeatingHandler;
import at.bestsolution.quti.handler.event.GetHandler;
import at.bestsolution.quti.handler.event.MoveHandler;
import at.bestsolution.quti.handler.event.SetDescriptionHandler;
import at.bestsolution.quti.handler.event.UncancelHandler;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
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

	@Inject
	public EventServiceImpl(GetHandler getHandler,
		CreateHandler createHandler,
		DeleteHandler deleteHandler,
		MoveHandler moveHandler,
		CancelHandler cancelHandler,
		UncancelHandler uncancelHandler,
		EndRepeatingHandler endRepeatHandler,
		SetDescriptionHandler setDescriptionHandler) {
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
	public Result<EventDTO> get(String calendarKey, String eventKey, ZoneId zone) {
		return getHandler.get(calendarKey, eventKey, zone);
	}

	@Override
	public Result<String> create(String calendarKey, EventNewDTO event) {
		return createHandler.create(calendarKey, event);
	}

	@Override
	public Result<Void> delete(String calendarKey, String eventKey) {
		return deleteHandler.delete(calendarKey, eventKey);
	}

	@Override
	public Result<Void> endRepeat(String calendarKey, String eventKey, LocalDate endDate) {
		return endRepeatHandler.endRepeat(calendarKey, eventKey, endDate);
	}

	@Override
	public Result<Void> move(String calendarKey, String eventKey, ZonedDateTime start, ZonedDateTime end) {
		return moveHandler.move(calendarKey, eventKey, start, end);
	}

	@Override
	public Result<Void> cancel(String calendarKey, String eventKey) {
		return cancelHandler.cancel(calendarKey, eventKey);
	}

	@Override
	public Result<Void> uncancel(String calendarKey, String eventKey) {
		return uncancelHandler.uncancel(calendarKey, eventKey);
	}

	@Override
	public Result<Void> setDescription(String calendarKey, String eventKey, String description) {
		return setDescriptionHandler.setDescription(calendarKey, eventKey, description);
	}

}
