// Generated by RSD - Do not modify
package at.bestsolution.quti.service.impl;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import jakarta.inject.Singleton;

import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.InvalidArgumentException;
import at.bestsolution.quti.service.model.Event;
import at.bestsolution.quti.service.model.EventNew;
import at.bestsolution.quti.service.NotFoundException;

@Singleton
public class EventServiceImpl implements EventService {
	private final CreateHandler createHandler;
	private final GetHandler getHandler;
	private final UpdateHandler updateHandler;
	private final DeleteHandler deleteHandler;
	private final CancelHandler cancelHandler;
	private final UncancelHandler uncancelHandler;
	private final MoveHandler moveHandler;
	private final EndRepeatHandler endRepeatHandler;
	private final DescriptionHandler descriptionHandler;

	public EventServiceImpl(CreateHandler createHandler, GetHandler getHandler, UpdateHandler updateHandler, DeleteHandler deleteHandler, CancelHandler cancelHandler, UncancelHandler uncancelHandler, MoveHandler moveHandler, EndRepeatHandler endRepeatHandler, DescriptionHandler descriptionHandler) {
		this.createHandler = createHandler;
		this.getHandler = getHandler;
		this.updateHandler = updateHandler;
		this.deleteHandler = deleteHandler;
		this.cancelHandler = cancelHandler;
		this.uncancelHandler = uncancelHandler;
		this.moveHandler = moveHandler;
		this.endRepeatHandler = endRepeatHandler;
		this.descriptionHandler = descriptionHandler;
	}

	@Override
	public String create(BuilderFactory _factory, String calendar, EventNew.Data event)
			throws NotFoundException,
			InvalidArgumentException{
		return createHandler.create(_factory, calendar, event);
	}

	@Override
	public Event.Data get(BuilderFactory _factory, String calendar, String key, ZoneId timezone)
			throws NotFoundException,
			InvalidArgumentException{
		return getHandler.get(_factory, calendar, key, timezone);
	}

	@Override
	public void update(BuilderFactory _factory, String calendar, String key, Event.Patch changes){
		updateHandler.update(_factory, calendar, key, changes);
	}

	@Override
	public void delete(BuilderFactory _factory, String calendar, String key){
		deleteHandler.delete(_factory, calendar, key);
	}

	@Override
	public void cancel(BuilderFactory _factory, String calendar, String key){
		cancelHandler.cancel(_factory, calendar, key);
	}

	@Override
	public void uncancel(BuilderFactory _factory, String calendar, String key){
		uncancelHandler.uncancel(_factory, calendar, key);
	}

	@Override
	public void move(BuilderFactory _factory, String calendar, String key, ZonedDateTime start, ZonedDateTime end){
		moveHandler.move(_factory, calendar, key, start, end);
	}

	@Override
	public void endRepeat(BuilderFactory _factory, String calendar, String key, LocalDate end){
		endRepeatHandler.endRepeat(_factory, calendar, key, end);
	}

	@Override
	public void description(BuilderFactory _factory, String calendar, String key, String description){
		descriptionHandler.description(_factory, calendar, key, description);
	}

	public interface CreateHandler {
		public String create(BuilderFactory _factory, String calendar, EventNew.Data event)
				throws NotFoundException,
				InvalidArgumentException;
	}
	public interface GetHandler {
		public Event.Data get(BuilderFactory _factory, String calendar, String key, ZoneId timezone)
				throws NotFoundException,
				InvalidArgumentException;
	}
	public interface UpdateHandler {
		public void update(BuilderFactory _factory, String calendar, String key, Event.Patch changes);
	}
	public interface DeleteHandler {
		public void delete(BuilderFactory _factory, String calendar, String key);
	}
	public interface CancelHandler {
		public void cancel(BuilderFactory _factory, String calendar, String key);
	}
	public interface UncancelHandler {
		public void uncancel(BuilderFactory _factory, String calendar, String key);
	}
	public interface MoveHandler {
		public void move(BuilderFactory _factory, String calendar, String key, ZonedDateTime start, ZonedDateTime end);
	}
	public interface EndRepeatHandler {
		public void endRepeat(BuilderFactory _factory, String calendar, String key, LocalDate end);
	}
	public interface DescriptionHandler {
		public void description(BuilderFactory _factory, String calendar, String key, String description);
	}
}
