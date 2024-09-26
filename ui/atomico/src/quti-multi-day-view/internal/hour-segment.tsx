import { Fragment } from "atomico";
import { SCALE } from "../types";
import { classesString, Range } from "../utils";

type HourSegmentProps = {
  hour: number;
  scale: SCALE;
  showText: boolean;
  workhours: Range<number>;
  hoursCount: number;
};

export function HourSegment(props: HourSegmentProps) {
  const hourText = props.showText ? (
    <div class="hour-text-positioner">
      <span class="hour-text-text">
        {props.hour < 10 ? "0" + props.hour + ":00" : props.hour + ":00"}
      </span>
    </div>
  ) : undefined;

  return (
    <Fragment>
      <div
        class={classesString({
          "hour-segment": true,
          "hour-segment-work-hour":
            props.hour >= props.workhours.min &&
            props.hour < props.workhours.max,
          "hour-segment-dashed": true,
          "hour-segment-top-none-working":
            props.hour < props.workhours.min + 1 ||
            props.hour > props.workhours.max - 1,
          "hour-segment-bottom-none-working":
            props.hour < props.workhours.min ||
            props.hour > props.workhours.max - 1,
        })}
      >
        {hourText}
      </div>
      <div
        class={classesString({
          "hour-segment-work-hour":
            props.hour >= props.workhours.min &&
            props.hour < props.workhours.max,
        })}
      ></div>
    </Fragment>
  );
}
