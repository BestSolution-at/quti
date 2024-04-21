package at.bestsolution.quti.handler.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.Utils.Result;
import at.bestsolution.quti.handler.BaseHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class EndRepeatingHandler extends BaseHandler {
	@Inject
	public EndRepeatingHandler(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> endRepeat(UUID calendarKey, UUID eventKey, LocalDate endDate) {
		var em = em();

		var event = EventUtils.event(em, calendarKey, eventKey);

		if( event == null ) {
			return Result.notFound("No event with key '%s' was found in calendar '%s'", eventKey, calendarKey);
		} else if( event.repeatPattern == null ) {
			return Result.invalidContent("%s is not a repeating event", eventKey);
		}

		var endDatetime = Utils.atEndOfDay(ZonedDateTime.of(endDate, LocalTime.NOON, event.repeatPattern.recurrenceTimezone));

		if( endDatetime.isBefore(event.repeatPattern.startDate) ) {
			return Result.invalidContent(
				"Repeating end '%s' of '%s' is before the repeat start '%s'",
				endDatetime.toLocalDate(),
				eventKey,
				event.repeatPattern.startDate.toLocalDate()
			);
		}
		event.repeatPattern.endDate = endDatetime;

		em.persist(event);

		return Result.OK;
	}
}
