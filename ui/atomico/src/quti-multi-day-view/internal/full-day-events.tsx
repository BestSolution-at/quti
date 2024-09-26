import {
  CalendarDate,
  getLocalTimeZone,
  toCalendarDate,
} from "@internationalized/date";
import { LaneLayout, LaneLayoutEntry, MONTH_AND_DAY } from "../utils";
import { EventEmitter, InternalEvent, QEvent } from "../types";

type FullDayEventsProps = {
  dates: CalendarDate[];
  layout: LaneLayout<LaneLayoutEntry<InternalEvent>>;
  eventFocus: EventEmitter<QEvent>;
  eventBlur: EventEmitter<QEvent>;
};

export function FullDayEvents(props: FullDayEventsProps) {
  return (
    <div
      class="header-full-day-container"
      style={{ "--day-max-lanes": `${props.layout.maxLanes}` }}
    >
      {props.dates.map(() => (
        <div class="header-full-day-column"></div>
      ))}
      {props.layout.entries.map((e) => (
        <FullDayEntry
          maxLanes={props.layout.maxLanes}
          startDate={props.dates[0]}
          endDate={props.dates[props.dates.length - 1]}
          layoutEntry={e}
          eventFocus={props.eventFocus}
          eventBlur={props.eventBlur}
        ></FullDayEntry>
      ))}
    </div>
  );
}

type FullDayEntryProps = {
  startDate: CalendarDate;
  endDate: CalendarDate;
  maxLanes: number;
  layoutEntry: LaneLayoutEntry<InternalEvent>;
  eventFocus: EventEmitter<QEvent>;
  eventBlur: EventEmitter<QEvent>;
};

const FullDayEntry = (props: FullDayEntryProps) => {
  const top = (props.layoutEntry.lane.startLane / props.maxLanes) * 100 + "%";
  const bottom =
    "calc(" +
    (100 - ((props.layoutEntry.lane.startLane + 1) / props.maxLanes) * 100) +
    "% + 2px)";
  const left = "calc(" + props.layoutEntry.dimension.min + "% + 4px)";
  const right = "calc(" + (100 - props.layoutEntry.dimension.max) + "% + 3px)";

  const event = props.layoutEntry.data;
  const start = toCalendarDate(event.start);
  const end = toCalendarDate(event.end);
  const leadText =
    props.startDate.compare(start) > 0
      ? MONTH_AND_DAY.format(start.toDate(getLocalTimeZone())).replace(
          /\./g,
          "",
        )
      : undefined;
  const endText =
    props.endDate.compare(end) < 0
      ? MONTH_AND_DAY.format(end.toDate(getLocalTimeZone())).replace(/\./g, "")
      : undefined;

  const chevronLeft = (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 320 512"
      aria-hidden="true"
      style={{ height: "1em", verticalAlign: "-0.125em" }}
    >
      <description>
        Font Awesome Free 6.5.1 by @fontawesome - https://fontawesome.com
        License - https://fontawesome.com/license/free Copyright 2024 Fonticons,
        Inc.
      </description>
      <path
        fill="currentColor"
        d="M9.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l192 192c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L77.3 256 246.6 86.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-192 192z"
      />
    </svg>
  );

  const chevronRight = (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 320 512"
      aria-hidden="true"
      style={{ height: "1em", verticalAlign: "-0.125em" }}
    >
      <description>
        Font Awesome Free 6.5.1 by @fontawesome - https://fontawesome.com
        License - https://fontawesome.com/license/free Copyright 2024 Fonticons,
        Inc.
      </description>
      <path
        fill="currentColor"
        d="M310.6 233.4c12.5 12.5 12.5 32.8 0 45.3l-192 192c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3L242.7 256 73.4 86.6c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0l192 192z"
      />
    </svg>
  );

  const part = event.type ? `event day-event ${event.type}` : "event day-event";
  const classes = ["event", "day-event"];
  if (event.type) {
    classes.push(event.type);
  }

  const onFocus = () => {
    props.eventFocus(event.originalEvent);
  };

  const onBlur = (e: FocusEvent) => {
    props.eventBlur(event.originalEvent);
  };

  return (
    <div
      part={part}
      class={classes.join(" ")}
      tabIndex={0}
      style={{ top, bottom, left, right }}
      onfocus={onFocus}
      onblur={onBlur}
    >
      {/* Create a new stacking context so that we can use :after/:before */}
      <div class="day-event-text-container">
        {leadText && (
          <span class="day-event-text-lead-text">
            {chevronLeft} {leadText}
          </span>
        )}
        <span class="day-event-text">{props.layoutEntry.data.subject}</span>
        {endText && (
          <span class="day-event-text-end-text">
            {endText} {chevronRight}
          </span>
        )}
      </div>
    </div>
  );
};
