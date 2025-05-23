package at.bestsolution.quti.calendar.service.jpa.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.NotFoundException;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.impl.EventServiceImpl;
import at.bestsolution.quti.calendar.service.jpa.BaseHandler;
import at.bestsolution.quti.calendar.service.jpa.RepeatUtils;
import at.bestsolution.quti.calendar.service.jpa.event.utils.EventUtils;
import at.bestsolution.quti.calendar.service.jpa.model.modification.EventModificationGenericEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class SetDescriptionHandlerJPA extends BaseHandler implements EventServiceImpl.DescriptionHandler {
	@Inject
	public SetDescriptionHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void description(BuilderFactory factory, String calendarKey, String eventKey,
			String description) {
		var seriesSep = eventKey.indexOf('_');

		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = seriesSep == -1 ? Utils.parseUUID(eventKey, "in path")
				: Utils.parseUUID(eventKey.substring(0, seriesSep), "in path");
		var parsedOriginalDate = seriesSep == -1 ? null
				: Utils.parseLocalDate(eventKey.substring(seriesSep + 1), "in path");

		if (parsedOriginalDate == null) {
			setDescriptionSingleEvent(parsedCalendarKey, parsedEventKey, description);
		} else {
			setDescriptionInSeries(parsedCalendarKey, parsedEventKey, parsedOriginalDate,
					description);
		}
	}

	private void setDescriptionSingleEvent(UUID calendarKey, UUID eventKey, String description) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);
		if (event == null) {
			throw new NotFoundException(
					"No event with master-key '%s' was found in calendar '%s'".formatted(eventKey, calendarKey));
		}

		event.description = description;
		em.persist(event);
	}

	private void setDescriptionInSeries(UUID calendarKey, UUID eventKey, LocalDate original, String description) {
		var em = em();
		var event = EventUtils.event(em, calendarKey, eventKey);

		var startDatetime = ZonedDateTime.of(original, LocalTime.MIN, event.repeatPattern.recurrenceTimezone);
		var endDatetime = ZonedDateTime.of(original, LocalTime.MAX, event.repeatPattern.recurrenceTimezone);

		if (!RepeatUtils.fromRepeat(event, startDatetime, endDatetime).anyMatch(d -> d.equals(original))) {
			throw new NotFoundException("Event is not repeated on the given date %s".formatted(original));
		}

		var entity = event.modifications.stream()
				.filter(e -> e instanceof EventModificationGenericEntity)
				.map(e -> (EventModificationGenericEntity) e)
				.filter(e -> e.date.equals(original))
				.findFirst()
				.orElseGet(() -> new EventModificationGenericEntity());

		entity.date = original;
		entity.event = event;
		entity.description = description;

		em.persist(entity);
	}
}
