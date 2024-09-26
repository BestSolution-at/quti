import {
  CalendarDate,
  isSameDay,
  Time,
  toCalendarDate,
} from "@internationalized/date";
import { InternalEvent, SCALE } from "../types";
import { computeTimeLayout, Range, toTimeFraction } from "../utils";
import { HourSegment } from "./hour-segment";
import { TimeEventEntry } from "./time-event-entry";

type ContentColumnProps = {
  date: CalendarDate;
  events: readonly InternalEvent[];
  hours: number[];
  scale: SCALE;
  workhours: Range<number>;
  hoursCount: number;
  /*eventFocus: EventEmitter<QEvent>;
      eventBlur: EventEmitter<QEvent>;*/
};

export function ContentColumn(props: ContentColumnProps) {
  const layout = computeTimeLayout(
    props.date,
    props.events.filter((e) => {
      return (
        e.fullday !== true &&
        (isSameDay(toCalendarDate(e.start), props.date) ||
          isSameDay(toCalendarDate(e.end), props.date))
      );
    }),
    props.hoursCount,
  );
  const startShift =
    props.hours[0] === 0
      ? 0
      : toTimeFraction(new Time(props.hours[0], 0), props.hoursCount);
  return (
    <div class="content-column">
      {/* required because of padding above it */}
      <div class="content-column-content">
        {props.hours.map((hour) => (
          <HourSegment
            hour={hour}
            scale={props.scale}
            showText={false}
            workhours={props.workhours}
            hoursCount={props.hoursCount}
          />
        ))}
        {layout.entries.map((entry) => (
          <TimeEventEntry entry={entry} startShift={startShift} />
        ))}
      </div>
    </div>
  );
}
