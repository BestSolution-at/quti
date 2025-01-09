package at.bestsolution.quti.service.jpa.event.utils;

import static at.bestsolution.quti.service.jpa.event.utils.RepeatBaseUpdater.*;

import java.util.List;

import at.bestsolution.quti.service.Result;
import at.bestsolution.quti.service.dto.EventRepeatWeeklyDTO;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatWeeklyEntity;
import jakarta.persistence.EntityManager;

public class RepeatWeeklyUpdater {
	public static Result<EventRepeatEntity> handleUpdateRepeatWeekly(
			EntityManager em,
			EventEntity event,
			EventRepeatEntity repeatPattern,
			EventRepeatWeeklyDTO.Patch p) {
		if (repeatPattern instanceof EventRepeatWeeklyEntity r) {
			var status = EventRepeatWeeklyDTO.Patch.ifTimeZone(p, v -> handleTimezone(r, v), Result.OK);

			if (status.isNotOk()) {
				return status.toAny();
			}

			status = EventRepeatWeeklyDTO.Patch.ifEndDate(p, v -> handleEndDate(r, v), Result.OK);

			if (status.isNotOk()) {
				return status.toAny();
			}

			status = EventRepeatWeeklyDTO.Patch.ifInterval(p, v -> handleInterval(r, v), Result.OK);

			if (status.isNotOk()) {
				return status.toAny();
			}

			status = EventRepeatWeeklyDTO.Patch.ifDaysOfWeek(p, v -> {
				r.daysOfWeek(v == null ? List.of() : v);
				return Result.OK;
			}, Result.OK);

			clearModificationProps(
					em,
					event,
					List.of(
							EventRepeatWeeklyDTO.Patch.Props.INTERVAL,
							EventRepeatWeeklyDTO.Patch.Props.TIMEZONE,
							EventRepeatWeeklyDTO.Patch.Props.DAYSOFWEEK),
					p::isSet);
			return Result.ok(repeatPattern);
		}

		return Result.invalidContent("The current repeat-pattern is not weekly");
	}
}
