package at.bestsolution.quti.service.jpa.event;

import java.time.LocalDate;
import java.util.UUID;

import org.jboss.logging.Logger;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.jpa.model.modification.EventModificationCanceledEntity;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class UncancelHandlerJPA extends BaseHandler implements EventService.UncancelHandler {
	private static final Logger LOG = Logger.getLogger(UncancelHandlerJPA.class);

	@Inject
	public UncancelHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> uncancel(BuilderFactory factory, String calendarKey, String eventKey) {
		var seriesSep = eventKey.indexOf('_');

		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = seriesSep == -1 ? Utils.parseUUID(eventKey, "in path")
				: Utils.parseUUID(eventKey.substring(0, seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? Result.<LocalDate>ok(null)
				: Utils.parseLocalDate(eventKey.substring(seriesSep + 1), "in path");

		if (parsedCalendarKey.isNotOk()) {
			return parsedCalendarKey.toAny();
		}
		if (parsedEventKey.isNotOk()) {
			return parsedEventKey.toAny();
		}
		if (parsedOriginalDate.isNotOk()) {
			return parsedOriginalDate.toAny();
		}

		if (parsedOriginalDate.value() == null) {
			return uncancelSingleEvent(parsedCalendarKey.value(), parsedEventKey.value());
		}
		return uncancelEventInSeries(parsedCalendarKey.value(), parsedEventKey.value(), parsedOriginalDate.value());
	}

	private Result<Void> uncancelSingleEvent(UUID calendarKey, UUID eventKey) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		if (event == null) {
			LOG.infof("Could not find an event '%s' in calendar '%s'", eventKey, calendarKey);
			return Result.notFound("No event with master-key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		event.modifications.stream()
				.filter(e -> e instanceof EventModificationCanceledEntity)
				.findFirst()
				.ifPresent(e -> em.remove(e));

		return Result.OK;
	}

	private Result<Void> uncancelEventInSeries(UUID calendarKey, UUID eventKey, LocalDate original) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		if (event == null) {
			LOG.infof("Could not find an event '%s' in calendar '%s'", eventKey, calendarKey);
			return Result.notFound("No event with master-key '%s' was found in calendar '%s'", eventKey, calendarKey);
		}

		event.modifications.stream()
				.filter(e -> e instanceof EventModificationCanceledEntity)
				.map(e -> (EventModificationCanceledEntity) e)
				.filter(e -> e.date.equals(original))
				.findFirst()
				.ifPresent(e -> em.remove(e));

		return Result.OK;
	}
}
