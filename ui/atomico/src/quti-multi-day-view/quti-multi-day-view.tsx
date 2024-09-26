import { CalendarDate } from "@internationalized/date";
import { c, css, Props, Type, useEffect, useEvent, useMemo, useRef, useState } from "atomico";
import {
  allErrors,
  checkAllPropsNonError,
  checkInteger,
  checkIntegerMinMax,
  checkISODate,
  checkScale,
  computeDates,
  computeDayLayout,
  computeInternalHours,
  createDayRangeFilter,
  defaultStartDate,
  isEvent,
  mapToInternalEvent,
  Range,
  scaleToHourPartition,
} from "./utils";
import { QEvent, QEvents, SCALE } from "./types";
import { Header } from "./internal/header";
import { FullDayEvents } from "./internal/full-day-events";
import { HourSegment } from "./internal/hour-segment";
import { ContentColumn } from "./internal/content-column";

function qutiMultiDayView(props: Props<typeof qutiMultiDayView>) {
  const checkedEvents = useMemo(() => {
    if (props.events === undefined) {
      return [];
    }
    return props.events.filter(isEvent);
  }, [props.events]);
  const validatedProps = {
    startDate: checkISODate(props.startDate, "start-date"),
    days: checkInteger(props.days, "days", 1),
    events: checkedEvents,
    hoursMin: checkInteger(props.hoursMin, "hours-min", 0),
    hoursMax: checkIntegerMinMax(props.hoursMax, "hours-max", 1, 24),
    scale: checkScale(props.scale),
    workingHoursMin: checkInteger(
      props.workingHoursMin,
      "working-hours-min",
      0,
    ),
    workingHoursMax: checkIntegerMinMax(
      props.workingHoursMax,
      "working-hours-max",
      1,
      24,
    ),
  };

  if (!checkAllPropsNonError(validatedProps)) {
    return (
      <host shadowDom>
        Invalid input:
        <ul>
          {allErrors(validatedProps).map((e) => (
            <li>{e.error}</li>
          ))}
        </ul>
      </host>
    );
  }

  return (
    <host shadowDom>
      <InternalQutiMultiDayView
        startDate={validatedProps.startDate}
        days={validatedProps.days}
        events={validatedProps.events}
        hoursMin={validatedProps.hoursMin}
        hoursMax={validatedProps.hoursMax}
        workingHoursMin={validatedProps.workingHoursMin}
        workingHoursMax={validatedProps.workingHoursMax}
        scale="half-hour"
      />
    </host>
  );
}

function computeScrollbarInsets(contentAreaElement: HTMLElement) {
  if (contentAreaElement) {
    if (
      contentAreaElement.offsetWidth !==
      contentAreaElement.clientWidth
    ) {
      return contentAreaElement.offsetWidth - contentAreaElement.clientWidth;
    } else {
      return 0;
    }
  }
}

function InternalQutiMultiDayView(props: {
  startDate: CalendarDate;
  days: number;
  events: QEvents;
  hoursMin: number;
  hoursMax: number;
  workingHoursMin: number;
  workingHoursMax: number;
  scale: SCALE;
}) {
  const [ scrollbarInsets, setScrollbarInsets ] = useState(0);
  const ref = useRef();
  const eventFocus = useEvent('event-focus', { bubbles: true, composed: true });
  const eventBlur = useEvent('event-blur', { bubbles: true, composed: true });

  useEffect(() => {
    const { current } = ref;
    const resizeObserver = new ResizeObserver( () => {
      const inset = computeScrollbarInsets(current);
      setScrollbarInsets(inset);
    });
    resizeObserver.observe(current);
    return () => {
      resizeObserver.disconnect();
    };
  }, []);

  const internalDates = useMemo(() => {
    return computeDates(props.startDate, props.days);
  }, [props.days, props.startDate]);

  const internalEvents = useMemo(() => {
    return props.events.map(mapToInternalEvent);
  }, [props.events]);

  const startDate = internalDates[0];
  const endDate = internalDates[internalDates.length - 1];

  const dayEntries = useMemo(() => {
    return internalEvents.filter(createDayRangeFilter(startDate, endDate));
  }, [internalDates, startDate, endDate]);
  const dayLayout = useMemo(() => {
    return dayEntries.length > 0
      ? computeDayLayout(startDate, internalDates.length, dayEntries)
      : undefined;
  }, [startDate, internalDates, dayEntries]);
  const internalHours = useMemo(
    () => computeInternalHours(props.hoursMin, props.hoursMax),
    [props.hoursMin, props.hoursMax],
  );
  const internalWorkhours: Range<number> = useMemo(() => {
    return { min: props.workingHoursMin, max: props.workingHoursMax };
  }, [props.workingHoursMin, props.workingHoursMax]);

  return (
    <div
      class="overflow-wrapper"
      style={{
        "--day-count": `${internalDates.length}`,
        "--hours-count": `${internalHours.length}`,
        "--hour-partition": `${scaleToHourPartition(props.scale)}`,
      }}
    >
      <div class="main-content">
        <div
          class="header-area"
          style={{
            paddingRight: `${scrollbarInsets}px`,
          }}
        >
          <div class="days-area">
            <div class="days-area-header-area">
              {internalDates.map((d) => (
                <Header date={d} />
              ))}
            </div>
            {dayLayout && dayLayout.entries.length > 0 && (
              <FullDayEvents
                dates={internalDates}
                layout={dayLayout}
                eventFocus={eventFocus}
                eventBlur={eventBlur}
              ></FullDayEvents>
            )}
          </div>
        </div>
        <div class="content-area" ref={ref}>
          <div class="hours-column-area">
            <div class="hours-column-content">
              {internalHours.map((hour) => (
                <HourSegment
                  hour={hour}
                  scale={props.scale}
                  showText={true}
                  workhours={internalWorkhours}
                  hoursCount={internalHours.length}
                />
              ))}
            </div>
          </div>
          <div class="content-days-area">
            {internalDates.map((d) => (
              <ContentColumn
                hours={internalHours}
                workhours={internalWorkhours}
                scale={props.scale}
                date={d}
                events={internalEvents}
                hoursCount={internalHours.length}
              />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

qutiMultiDayView.props = {
  startDate: {
    type: String,
    value: defaultStartDate,
    reflect: true,
  },
  days: {
    type: Number,
    value: 7,
    reflect: true,
  },
  events: {
    type: Array,
    value: [] as QEvents,
    reflect: true,
  },
  workingHoursMin: {
    type: Number,
    value: 8,
    reflect: true,
  },
  workingHoursMax: {
    type: Number,
    value: 17,
    reflect: true,
  },
  hoursMin: {
    type: Number,
    value: 0,
    reflect: true,
  },
  hoursMax: {
    type: Number,
    value: 24,
    reflect: true,
  },
  scale: {
    type: String as Type<SCALE>,
    value: "half-hour",
    reflect: true,
  }
};

qutiMultiDayView.styles = css`
  :host {
    display: flex;
    flex-direction: column;
    overflow: hidden;

    --border-width-thin: 1px;
    --border-width-thick: 4px;

    /* stylelint-disable color-no-hex -- colors fow now in here */
    --text-event-color-focus: #ffffff;

    /* grays */
    --color-gray-50: #ffffff;
    --color-gray-100: #f8f8f8;
    --color-gray-200: #e6e6e6;
    --color-gray-300: #d5d5d5;
    --color-gray-400: #b1b1b1;

    /* blues */
    --color-blue-800: #147af3;
    --color-blue-1100: #004491;
    --color-red-800: #ea3829;
    --color-red-1100: #930000;
    --color-orange-800: #cb5d00;
    --color-orange-1100: #7a2f00;
    --color-yellow-800: #9b7800;
    --color-yellow-1100: #5b4300;
    --color-charteuse-800: #678800;
    --color-charteuse-1100: #3a4d00;
    --color-celery-800: #009112;
    --color-celery-1100: #00530d;
    --color-green-800: #008f5d;
    --color-green-1100: #005132;
    --color-seafoam-800: #008c87;
    --color-seafoam-1100: #0c4f4c;
    --color-cyan-800: #0086b4;
    --color-cyan-1100: #004a73;
    --color-indigo-800: #686df4;
    --color-indigo-1100: #3236a8;
    --color-purple-800: #9d57f4;
    --color-purple-1100: #5d13b7;
    --color-fuchsia-800: #cd3ace;
    --color-fuchsia-1100: #800081;
    --color-magenta-800: #de3d82;
    --color-magenta-1100: #8e0045;

    /* stylelint-enablecolor-no-hex */

    /* main area */
    --main-background-color: var(--color-gray-50);
    --outline-border-color: var(--color-gray-200);
    --outline-border-width: var(--border-width-thin);

    /* header stuff */
    --header-column-border-color: var(--color-gray-200);
    --header-area-bottom-border-color: var(--color-gray-300);
    --header-today-marker-border-color: var(--color-blue-800);

    /* content stuff */
    --content-time-column-width: 7ch;
    --content-background-color: var(--color-gray-100);
    --content-column-border-color: var(--color-gray-400);
    --content-row-border-color-none-working: var(--color-gray-400);
    --content-row-border-color-working: var(--color-gray-300);
    --content-top-spacing: 0.5lh;
    --content-column-min-width: 15ch;

    /* event stuff */
    --event-color-type-1-bg: var(--color-blue-800);
    --event-color-type-1-fg: var(--color-blue-1100);
    --event-color-type-2-bg: var(--color-red-800);
    --event-color-type-2-fg: var(--color-red-1100);
    --event-color-type-3-bg: var(--color-orange-800);
    --event-color-type-3-fg: var(--color-orange-1100);
    --event-color-type-4-bg: var(--color-yellow-800);
    --event-color-type-4-fg: var(--color-yellow-1100);
    --event-color-type-5-bg: var(--color-charteuse-800);
    --event-color-type-5-fg: var(--color-charteuse-1100);
    --event-color-type-6-bg: var(--color-celery-800);
    --event-color-type-6-fg: var(--color-celery-1100);
    --event-color-type-7-bg: var(--color-green-800);
    --event-color-type-7-fg: var(--color-green-1100);
    --event-color-type-8-bg: var(--color-seafoam-800);
    --event-color-type-8-fg: var(--color-seafoam-1100);
    --event-color-type-9-bg: var(--color-cyan-800);
    --event-color-type-9-fg: var(--color-cyan-1100);
    --event-color-type-10-bg: var(--color-indigo-800);
    --event-color-type-10-fg: var(--color-indigo-1100);
    --event-color-type-11-bg: var(--color-purple-800);
    --event-color-type-11-fg: var(--color-purple-1100);
    --event-color-type-12-bg: var(--color-fuchsia-800);
    --event-color-type-12-fg: var(--color-fuchsia-1100);
    --event-color-type-13-bg: var(--color-magenta-800);
    --event-color-type-13-fg: var(--color-magenta-1100);

    /* fullday event stuff */
    --full-day-event-height: 1.2lh;

    border-width: var(--outline-border-width);
    border-style: solid;
    border-color: var(--outline-border-color);
  }

  .overflow-wrapper {
    display: flex;
    flex-direction: column;
    overflow-y: hidden;
  }

  .main-content {
    background-color: var(--main-background-color);
    display: flex;
    flex-direction: column;
    min-width: calc(
      var(--day-count) * var(--content-column-min-width) +
        var(--content-time-column-width)
    );
    overflow-y: hidden;
  }

  .header-area {
    display: grid;
    grid-template-columns: var(--content-time-column-width) auto;
    border-bottom: var(--border-width-thick) solid
      var(--header-area-bottom-border-color);
  }

  .header-area > .days-area {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    grid-column: 2;
  }

  .header-area > .days-area > .days-area-header-area {
    display: grid;
    grid-template-columns: repeat(var(--day-count, 1), 1fr);
  }

  .day-header-container {
    display: flex;
  }

  .day-header-text-container {
    position: relative;
    display: flex;
    flex-grow: 1;
    align-items: flex-end;
    border-left: var(--border-width-thin) solid
      var(--header-column-border-color);
  }

  .day-header-text-container.today::after {
    content: "";
    position: absolute;
    top: 0;
    left: calc(var(--border-width-thin) * -1);
    right: 0;
    border-top-style: solid;
    border-top-width: 4px;
    border-color: var(--header-today-marker-border-color);
  }

  .day-header-text-node-container {
    display: flex;
    align-items: baseline;
    column-gap: 8px;
    padding-left: 8px;
    margin-top: 10px;
    margin-bottom: 8px;
  }

  .day-text-date {
    font-weight: 600;
    font-size: 1.4em;
  }

  .day-header-text-container.today .day-text-date {
    color: var(--color-blue-1100);
  }

  .day-text-week {
    font-weight: 400;
  }

  .header-full-day-container {
    display: grid;
    grid-template-columns: repeat(var(--day-count, 1), 1fr);
    position: relative;
    min-height: calc(var(--day-max-lanes) * var(--full-day-event-height));
  }

  .header-full-day-column {
    border-left: var(--border-width-thin) solid
      var(--header-column-border-color);
  }

  .content-area {
    display: grid;
    grid-template-columns: var(--content-time-column-width) auto;
    overflow-y: auto;
    flex-grow: 1;
  }

  .hours-column-area {
    display: flex;
    flex-direction: column;
    padding-top: var(--content-top-spacing);
  }

  .hours-column-content {
    min-height: calc(var(--hours-count) * var(--hour-partition) * 1.5lh);
    display: grid;
    grid-template-rows: repeat(
      calc(var(--hours-count) * var(--hour-partition)),
      1fr
    );
    flex-grow: 1;
  }

  .hour-segment.hour-segment-top-none-working {
    border-top-color: var(--content-row-border-color-none-working);
  }

  .hour-segment:not(.hour-segment-top-none-working) {
    border-top-color: var(--content-row-border-color-working);
  }

  .hour-segment.hour-segment-bottom-none-working {
    border-bottom-color: var(--content-row-border-color-none-working);
  }

  .hour-segment:not(.hour-segment-bottom-none-working) {
    border-bottom-color: var(--content-row-border-color-working);
  }

  .hour-segment-work-hour {
    background-color: var(--main-background-color);
  }

  .hour-segment-dashed {
    border-top-width: var(--border-width-thin);
    border-top-style: solid;
    border-bottom-width: var(--border-width-thin);
    border-bottom-style: dashed;
  }

  .hour-text-positioner {
    translate: 0 -0.5lh;
    text-align: center;
  }

  .hour-text-text {
    font-weight: 400;
    background-color: var(--main-background-color);
    padding-left: 2px;
    padding-right: 2px;
  }

  .content-days-area {
    display: grid;
    grid-template-columns: repeat(var(--day-count, 1), 1fr);
  }

  .content-column {
    display: flex;
    position: relative;
    padding-top: var(--content-top-spacing);
    background-color: var(--content-background-color);
    border-left: var(--border-width-thin) solid
      var(--content-column-border-color);
    flex-direction: column;
    flex-grow: 1;
  }

  .content-column-content {
    position: relative;
    min-height: calc(var(--hours-count) * var(--hour-partition) * 30px);
    display: grid;
    grid-template-rows: repeat(
      calc(var(--hours-count) * var(--hour-partition)),
      1fr
    );
    flex-grow: 1;
  }

  .event {
    --event-color-bg: var(--event-color-type-1-bg);
    --event-color-fg: var(--event-color-type-1-fg);
  }

  .event.event.type-1 {
    --event-color-bg: var(--event-color-type-1-bg);
    --event-color-fg: var(--event-color-type-1-fg);
  }

  .event.event.type-2 {
    --event-color-bg: var(--event-color-type-2-bg);
    --event-color-fg: var(--event-color-type-2-fg);
  }

  .event.event.type-3 {
    --event-color-bg: var(--event-color-type-3-bg);
    --event-color-fg: var(--event-color-type-3-fg);
  }

  .event.event.type-4 {
    --event-color-bg: var(--event-color-type-4-bg);
    --event-color-fg: var(--event-color-type-4-fg);
  }

  .event.event.type-5 {
    --event-color-bg: var(--event-color-type-5-bg);
    --event-color-fg: var(--event-color-type-5-fg);
  }

  .event.event.type-6 {
    --event-color-bg: var(--event-color-type-6-bg);
    --event-color-fg: var(--event-color-type-6-fg);
  }

  .event.event.type-7 {
    --event-color-bg: var(--event-color-type-7-bg);
    --event-color-fg: var(--event-color-type-7-fg);
  }

  .event.event.type-8 {
    --event-color-bg: var(--event-color-type-8-bg);
    --event-color-fg: var(--event-color-type-8-fg);
  }

  .event.event.type-9 {
    --event-color-bg: var(--event-color-type-9-bg);
    --event-color-fg: var(--event-color-type-9-fg);
  }

  .event.event.type-10 {
    --event-color-bg: var(--event-color-type-10-bg);
    --event-color-fg: var(--event-color-type-10-fg);
  }

  .event.event.type-11 {
    --event-color-bg: var(--event-color-type-11-bg);
    --event-color-fg: var(--event-color-type-11-fg);
  }

  .event.event.type-12 {
    --event-color-bg: var(--event-color-type-12-bg);
    --event-color-fg: var(--event-color-type-12-fg);
  }

  .event.event.type-13 {
    --event-color-bg: var(--event-color-type-13-bg);
    --event-color-fg: var(--event-color-type-13-fg);
  }

  .time-event {
    display: flex;
    position: absolute;
    scroll-margin-top: 10px;
    scroll-margin-bottom: 10px;
    outline: none;
    color: var(--event-color-fg);
    overflow: clip;
  }

  .time-event-content {
    border-left: 4px solid var(--event-color-fg);
    border-top-left-radius: 2px;
    border-bottom-left-radius: 2px;
    display: flex;
  }

  .time-event::before {
    content: "";
    position: absolute;
    inset: 0 0 0 4px;
    background-color: var(--event-color-bg);
    border-top-right-radius: 2px;
    border-bottom-right-radius: 2px;
    opacity: 0.24;
  }

  .time-event:hover::before {
    opacity: 0.48;
  }

  .time-event:focus::before {
    opacity: 1;
  }

  .time-event:focus {
    overflow: visible;
    color: var(--text-event-color-focus);
    /* stylelint-disable-next-line property-disallowed-list -- move event above others if overlapping */
    z-index: 1;
  }

  .time-event .event-text-positioner {
    max-height: 100%;
    max-width: 100%;
    position: sticky;
    top: 0;
    display: flex;
    align-self: flex-start;
    font-size: 1em;
    line-height: 1em;
    flex-grow: 1;
    overflow: hidden;
  }

  .time-event:focus .event-text-positioner {
    max-height: unset;
  }

  .time-event .event-text-positioner .event-text {
    padding-left: 10px;
    padding-right: 5px;
    padding-top: 2px;
    border-radius: 2px;
    flex-grow: 1;
    display: flex;
    column-gap: 10px;
    flex-wrap: wrap;
    align-items: center;
    overflow: hidden;
  }

  .time-event:focus .event-text {
    background-color: var(--event-color-bg);
  }

  .time-event .event-text-positioner .event-text .subject {
    font-weight: 600;
  }

  .time-event .event-text-positioner .event-text .time {
    display: inline-block;
    font-size: 0.8em;
  }

  .day-event {
    position: absolute;
    display: flex;
    overflow: hidden;
    outline: none;
    white-space: nowrap;
    padding-left: 10px;
    padding-right: 10px;
    color: var(--event-color-fg);
  }

  .day-event-text-container {
    position: relative; /* We want a new stacking container */
    display: flex;
    flex-grow: 1;
    align-items: center;
  }

  .day-event-text {
    font-weight: 600;
  }

  .day-event-text-lead-text,
  .day-event-text-end-text {
    font-weight: 300;
    font-size: 0.75em;
  }

  .day-event-text-lead-text {
    padding-right: 5px;
  }

  .day-event-text-end-text {
    margin-left: auto;
  }

  .day-event:focus {
    color: var(--text-event-color-focus);
  }

  .day-event::before {
    content: "";
    position: absolute;
    inset: 0;
    background-color: var(--event-color-bg);
    border-radius: 2px;
    opacity: 0.02;
  }

  .day-event:hover::before {
    opacity: 0.12;
  }

  .day-event:focus::before {
    opacity: 1;
  }

  .day-event::after {
    content: "";
    position: absolute;
    inset: 0;
    border-color: var(--event-color-bg);
    border-style: solid;
    border-width: 1px;
    border-radius: 2px;
    opacity: 0.5;
  }
`;

export const QutiMultiDayView = c(qutiMultiDayView);

customElements.define("quti-multi-day-view", QutiMultiDayView);
