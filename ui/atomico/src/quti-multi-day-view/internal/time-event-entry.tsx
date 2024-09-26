import { getLocalTimeZone } from "@internationalized/date";
import { InternalEvent } from "../types";
import { classesString, LaneLayoutEntry, TIME_FORMAT } from "../utils";

type TimeEventEntryProps = {
  entry: LaneLayoutEntry<InternalEvent>;
  startShift: number;
  /*eventFocus: EventEmitter<QEvent>;
      eventBlur: EventEmitter<QEvent>;*/
};

export function TimeEventEntry(props: TimeEventEntryProps) {
  const { entry } = props;
  const event = entry.data;
  const top = `calc(${entry.dimension.min - props.startShift}% + 2px)`;
  const bottom = `calc(${Math.max(0, 100 - entry.dimension.max + props.startShift)}% + 1px)`;
  const left = `${(entry.lane.startLane / entry.lane.maxLane) * 100}%`;
  const right = `calc(max(${100 - (entry.lane.endLane / entry.lane.maxLane) * 100}% + 2px, 10px))`;

  const timezone = getLocalTimeZone();
  const startTime = TIME_FORMAT.format(event.start.toDate(timezone));
  const endTime = TIME_FORMAT.format(event.end.toDate(timezone));

  const part = event.type
    ? `event time-event ${event.type}`
    : "event time-event";

  const classes = {
    event: true,
    "time-event": true,
  };

  if (event.type) {
    classes[event.type] = true;
  }

  return (
    <div
      class={classesString(classes)}
      part={part}
      tabIndex={0}
      style={{
        left,
        right,
        top,
        bottom,
      }}
    >
      <div class="time-event-content">
        <div class="event-text-positioner">
          <div class="event-text">
            <span class="subject">{event.subject}</span>
            <div class="time">
              {startTime} - {endTime}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
