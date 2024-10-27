package at.bestsolution.quti.service.jpa.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import at.bestsolution.quti.Utils;
import at.bestsolution.quti.service.EventService;
import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.jpa.BaseHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class EndRepeatingHandlerJPA extends BaseHandler implements EventService.EndRepeatingHandler {
	@Inject
	public EndRepeatingHandlerJPA(EntityManager em) {
		super(em);
	}

	@Transactional
	public Result<Void> endRepeat(String calendarKey, String eventKey, LocalDate endDate) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");
		var parsedEventKey = Utils.parseUUID(eventKey, "in path");

		if( parsedCalendarKey.isNotOk() ) {
			return parsedCalendarKey.toAny();
		}

		if( parsedEventKey.isNotOk() ) {
			return parsedEventKey.toAny();
		}

		var em = em();

		var event = EventUtils.event(em, parsedCalendarKey.value(), parsedEventKey.value());

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
