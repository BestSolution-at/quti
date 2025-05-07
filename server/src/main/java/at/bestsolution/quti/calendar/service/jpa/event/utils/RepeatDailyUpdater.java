// package at.bestsolution.quti.service.jpa.event.utils;

// import static
// at.bestsolution.quti.service.jpa.event.utils.RepeatBaseUpdater.*;

// import java.util.List;

// import at.bestsolution.quti.service.Result;
// import at.bestsolution.quti.service.model.EventRepeatDaily;
// import at.bestsolution.quti.service.jpa.model.EventEntity;
// import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
// import at.bestsolution.quti.service.jpa.model.repeat.EventRepeatDailyEntity;
// import jakarta.persistence.EntityManager;

// public class RepeatDailyUpdater {
// public static Result<EventRepeatEntity> handleUpdateRepeatDaily(
// EntityManager em,
// EventEntity event,
// EventRepeatEntity repeatPattern,
// EventRepeatDaily.Patch p) {
// System.err.println("====> Update");
// if (repeatPattern instanceof EventRepeatDailyEntity r) {
// System.err.println("Step 1");
// var status = EventRepeatDaily.Patch.ifTimeZone(p, v -> handleTimezone(r, v),
// Result.OK);

// if (status.isNotOk()) {
// return status.toAny();
// }

// System.err.println("Step 2");
// status = EventRepeatDaily.Patch.ifEndDate(p, v -> handleEndDate(r, v),
// Result.OK);

// if (status.isNotOk()) {
// return status.toAny();
// }

// System.err.println("Step 3");
// status = EventRepeatDaily.Patch.ifInterval(p, v -> handleInterval(r, v),
// Result.OK);

// if (status.isNotOk()) {
// return status.toAny();
// }

// clearModificationProps(
// em,
// event,
// List.of(
// EventRepeatDaily.Patch.Props.INTERVAL,
// EventRepeatDaily.Patch.Props.TIMEZONE),
// p::isSet);
// return Result.ok(repeatPattern);
// }
// return Result.invalidContent("The current repeat-pattern is not daily");
// }

// }
