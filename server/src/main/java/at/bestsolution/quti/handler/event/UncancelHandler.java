package at.bestsolution.quti.handler.event;

import java.time.LocalDate;
import java.util.UUID;

import org.jboss.logging.Logger;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.Utils.Result;
import at.bestsolution.quti.handler.BaseHandler;
import at.bestsolution.quti.model.modification.EventModificationCanceledEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class UncancelHandler extends BaseHandler {
private static final Logger LOG = Logger.getLogger(UncancelHandler.class);

	@Inject
	public UncancelHandler(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> uncancel(String calendarKey, String eventKey) {
		var seriesSep = eventKey.indexOf('_');

		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = seriesSep == -1 ? Utils.parseUUID(eventKey, "in path") : Utils.parseUUID(eventKey.substring(0,seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? Result.<LocalDate>ok(null) : Utils.parseLocalDate(eventKey.substring(seriesSep+1), "in path");

		if( parsedCalendarKey.isNotOk() ) {
			return parsedCalendarKey.toAny();
		}
		if( parsedEventKey.isNotOk() ) {
			return parsedEventKey.toAny();
		}
		if( parsedOriginalDate.isNotOk() ) {
			return parsedOriginalDate.toAny();
		}

		if( parsedOriginalDate.value() == null ) {
			return uncancelSingleEvent(parsedCalendarKey.value(), parsedEventKey.value());
		}
		return uncancelEventInSeries(parsedCalendarKey.value(), parsedEventKey.value(), parsedOriginalDate.value());
	}

	private Result<Void> uncancelSingleEvent(UUID calendarKey, UUID eventKey) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		if( event == null ) {
			LOG.infof("Could not find an event '%s' in calendar '%s'", eventKey, calendarKey);
			return Result.notFound("No event with master-key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		event.modifications.stream()
			.filter(e -> e instanceof EventModificationCanceledEntity)
			.findFirst()
			.ifPresent( e -> em.remove(e));

		return Result.OK;
	}

	private Result<Void> uncancelEventInSeries(UUID calendarKey, UUID eventKey, LocalDate original) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		if( event == null ) {
			LOG.infof("Could not find an event '%s' in calendar '%s'", eventKey, calendarKey);
			return Result.notFound("No event with master-key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		event.modifications.stream()
			.filter(e -> e instanceof EventModificationCanceledEntity)
			.map(e -> (EventModificationCanceledEntity)e)
			.filter( e -> e.date.equals(original))
			.findFirst()
			.ifPresent( e -> em.remove(e));

			return Result.OK;
	}
}
