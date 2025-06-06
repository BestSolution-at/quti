package at.bestsolution.quti.calendar.service.jpa.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import at.bestsolution.quti.calendar.service.BuilderFactory;
import at.bestsolution.quti.calendar.service.InvalidContentException;
import at.bestsolution.quti.calendar.service.NotFoundException;
import at.bestsolution.quti.calendar.service.Utils;
import at.bestsolution.quti.calendar.service.impl.EventServiceImpl;
import at.bestsolution.quti.calendar.service.jpa.BaseHandler;
import at.bestsolution.quti.calendar.service.jpa.event.utils.EventUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class EndRepeatingHandlerJPA extends BaseHandler implements EventServiceImpl.EndRepeatHandler {
	@Inject
	public EndRepeatingHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public void endRepeat(BuilderFactory factory, String calendarKey, String eventKey, LocalDate endDate) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

		var em = em();

		var event = EventUtils.event(em, parsedCalendarKey, parsedEventKey);

		if (event == null) {
			throw new NotFoundException("No event with key '%s' was found in calendar '%s'".formatted(eventKey, calendarKey));
		} else if (event.repeatPattern == null) {
			throw new InvalidContentException("%s is not a repeating event".formatted(eventKey));
		}

		var endDatetime = Utils
				.atEndOfDay(ZonedDateTime.of(endDate, LocalTime.NOON, event.repeatPattern.recurrenceTimezone));

		if (endDatetime.isBefore(event.repeatPattern.startDate)) {
			throw new InvalidContentException(
					"Repeating end '%s' of '%s' is before the repeat start '%s'".formatted(endDatetime.toLocalDate(),
							eventKey,
							event.repeatPattern.startDate.toLocalDate()));
		}
		event.repeatPattern.endDate = endDatetime;
		event.modifications
				.stream()
				.filter(m -> endDate.isBefore(m.date))
				.forEach(m -> em.remove(m));

		em.persist(event);
	}
}
