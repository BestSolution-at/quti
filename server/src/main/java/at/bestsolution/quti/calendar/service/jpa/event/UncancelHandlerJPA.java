package at.bestsolution.quti.calendar.service.jpa.event;

import java.time.LocalDate;
import java.util.UUID;

import org.jboss.logging.Logger;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.NotFoundException;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.impl.EventServiceImpl;
import at.bestsolution.quti.calendar.service.jpa.BaseHandler;
import at.bestsolution.quti.calendar.service.jpa.event.utils.EventUtils;
import at.bestsolution.quti.calendar.service.jpa.model.modification.EventModificationCanceledEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class UncancelHandlerJPA extends BaseHandler implements EventServiceImpl.UncancelHandler {
	private static final Logger LOG = Logger.getLogger(UncancelHandlerJPA.class);

	@Inject
	public UncancelHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void uncancel(BuilderFactory factory, String calendarKey, String eventKey) {
		var seriesSep = eventKey.indexOf('_');

		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = seriesSep == -1 ? Utils.parseUUID(eventKey, "in path")
				: Utils.parseUUID(eventKey.substring(0, seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? null
				: Utils.parseLocalDate(eventKey.substring(seriesSep + 1), "in path");

		if (parsedOriginalDate == null) {
			uncancelSingleEvent(parsedCalendarKey, parsedEventKey);
		} else {
			uncancelEventInSeries(parsedCalendarKey, parsedEventKey, parsedOriginalDate);
		}
	}

	private void uncancelSingleEvent(UUID calendarKey, UUID eventKey) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		if (event == null) {
			LOG.infof("Could not find an event '%s' in calendar '%s'", eventKey, calendarKey);
			throw new NotFoundException(
					"No event with master-key '%s' was found in calendar '%s'".formatted(eventKey, calendarKey));
		}

		event.modifications.stream()
				.filter(e -> e instanceof EventModificationCanceledEntity)
				.findFirst()
				.ifPresent(e -> em.remove(e));
	}

	private void uncancelEventInSeries(UUID calendarKey, UUID eventKey, LocalDate original) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		if (event == null) {
			LOG.infof("Could not find an event '%s' in calendar '%s'", eventKey, calendarKey);
			throw new NotFoundException(
					"No event with master-key '%s' was found in calendar '%s'".formatted(eventKey, calendarKey));
		}

		event.modifications.stream()
				.filter(e -> e instanceof EventModificationCanceledEntity)
				.map(e -> (EventModificationCanceledEntity) e)
				.filter(e -> e.date.equals(original))
				.findFirst()
				.ifPresent(e -> em.remove(e));
	}
}
