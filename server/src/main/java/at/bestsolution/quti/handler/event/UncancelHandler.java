package at.bestsolution.quti.handler.event;

import java.time.LocalDate;
import java.util.UUID;

import org.jboss.logging.Logger;

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
	public Result<Void> uncancel(UUID calendarKey, UUID eventKey, LocalDate original) {
		if( original == null ) {
			return uncancelSingleEvent(calendarKey, eventKey);
		}
		return uncancelEventInSeries(calendarKey, eventKey, original);
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
