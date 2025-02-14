package at.bestsolution.quti.service.jpa.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.jboss.logging.Logger;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.jpa.model.modification.EventModificationCanceledEntity;
import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.NotFoundException;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.RepeatUtils;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CancelHandlerJPA extends BaseHandler implements EventService.CancelHandler {
	private static final Logger LOG = Logger.getLogger(CancelHandlerJPA.class);

	@Inject
	public CancelHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void cancel(BuilderFactory factory, String calendarKey, String eventKey) {
		var seriesSep = eventKey.indexOf('_');

		var parsedCalendarKey = Utils._parseUUID(calendarKey, "in path");
		var parsedEventKey = seriesSep == -1 ? Utils._parseUUID(eventKey, "in path")
				: Utils._parseUUID(eventKey.substring(0, seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? null
				: Utils._parseLocalDate(eventKey.substring(seriesSep + 1), "in path");

		if (parsedOriginalDate == null) {
			cancelSingleEvent(parsedCalendarKey, parsedEventKey);
		} else {
			cancelEventInSeries(parsedCalendarKey, parsedEventKey, parsedOriginalDate);
		}
	}

	private void cancelSingleEvent(UUID calendarKey, UUID eventKey) {
		LOG.debugf("Canceling single event '%'", eventKey);
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);
		var date = LocalDate.EPOCH;

		var entity = event.modifications.stream()
				.filter(e -> e instanceof EventModificationCanceledEntity)
				.map(e -> (EventModificationCanceledEntity) e)
				.filter(e -> e.date.equals(date))
				.findFirst()
				.orElseGet(() -> new EventModificationCanceledEntity());

		entity.date = date;
		entity.event = event;

		em.persist(entity);
	}

	private void cancelEventInSeries(UUID calendarKey, UUID eventKey, LocalDate original) {
		LOG.debugf("Canceling series event '%' on '%s'", eventKey, original);

		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		LOG.tracef("Found event: %", event);

		if (event == null) {
			LOG.infof("Could not find an event '%s' in calendar '%s'", eventKey, calendarKey);
			throw new NotFoundException(
					"No event with master-key '%s' was found in calendar '%s'".formatted(eventKey, calendarKey));
		}

		var startDatetime = ZonedDateTime.of(original, LocalTime.MIN, event.repeatPattern.recurrenceTimezone);
		var endDatetime = ZonedDateTime.of(original, LocalTime.MAX, event.repeatPattern.recurrenceTimezone);

		if (!RepeatUtils.fromRepeat(event, startDatetime, endDatetime).anyMatch(d -> d.equals(original))) {
			throw new NotFoundException("Event is not repeated on the given date %s".formatted(original));
		}

		var entity = event.modifications.stream()
				.filter(e -> e instanceof EventModificationCanceledEntity)
				.map(e -> (EventModificationCanceledEntity) e)
				.filter(e -> e.date.equals(original))
				.findFirst()
				.orElseGet(() -> new EventModificationCanceledEntity());

		entity.date = original;
		entity.event = event;

		em.persist(entity);
	}
}
