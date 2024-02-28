import {
  CalendarDate,
  CalendarDateTime,
  DateFormatter,
  Time,
  getLocalTimeZone,
  isSameDay,
  isToday,
  parseDate,
  parseDateTime,
  parseDuration,
  parseTime,
  startOfMonth,
  startOfWeek,
  toCalendarDate,
  toTime,
  today,
} from '@internationalized/date';
import {
  Component,
  Fragment,
  Host,
  Prop,
  State,
  Watch,
  h,
} from '@stencil/core';

import {
  Duration,
  LaneLayout,
  LaneLayoutEntry,
  Range,
  computeLaneLayout,
  createDateRange,
  intersects,
  isBoolean,
  isString,
} from '../../utils/utils';

export type LocalDate = string;
export type LocalDatetime = string;

export type LocalDates = readonly LocalDate[];

export type Event = {
  readonly key: string;
  readonly start: LocalDatetime;
  readonly end: LocalDatetime;
  readonly subject: string;
  readonly fullday?: boolean;
  readonly type?: string;
};

export type Events = readonly Event[];

type InternalEvent = {
  readonly key: string;
  readonly start: CalendarDateTime;
  readonly end: CalendarDateTime;
  readonly subject: string;
  readonly fullday?: boolean;
  readonly type?: string;
};

export type SCALE =
  | 'hour'
  | 'half-hour'
  | 'quarter-hour'
  | '10-min'
  | '6-min'
  | '5-min';

function scaleToHourPartition(scale: SCALE): number {
  switch (scale) {
    case '10-min':
      return 6;
    case '5-min':
      return 12;
    case '6-min':
      return 10;
    case 'half-hour':
      return 2;
    case 'quarter-hour':
      return 4;
  }
  return 1;
}

function toTimeFraction(time: Time, hoursCount: number) {
  const hour = 100 / hoursCount;
  const minutes = hour / 60;
  return hour * time.hour + minutes * time.minute;
}

function toTimeLayoutEntry(
  date: CalendarDate,
  e: InternalEvent,
  hoursCount: number,
): LaneLayoutEntry<InternalEvent> {
  const from = isSameDay(date, e.start) ? toTime(e.start) : parseTime('00:00');
  const to = isSameDay(date, e.end) ? toTime(e.end) : parseTime('23:59'); //FIXME
  return {
    data: e,
    dimension: {
      min: toTimeFraction(from, hoursCount),
      max: toTimeFraction(to, hoursCount),
    },
    lane: { startLane: 0, endLane: 1, maxLane: 1 },
  };
}

function toDayFraction(totalDays: number, day: number) {
  return (100 / totalDays) * day;
}

function computeTimeLayout(
  date: CalendarDate,
  entries: readonly InternalEvent[],
  hoursCount: number,
): LaneLayout<LaneLayoutEntry<InternalEvent>> {
  return computeLaneLayout(
    entries.map(event => toTimeLayoutEntry(date, event, hoursCount)),
  );
}

function computeDayLayout(
  startDate: CalendarDate,
  days: number,
  entries: readonly InternalEvent[],
): LaneLayout<LaneLayoutEntry<InternalEvent>> {
  return computeLaneLayout(
    entries.map(event => toDayLayoutEntry(startDate, days, event)),
  );
}

function toDayLayoutEntry(
  start: CalendarDate,
  totalDays: number,
  e: InternalEvent,
): LaneLayoutEntry<InternalEvent> {
  parseDuration;
  const from = Math.max(
    0,
    Duration.between(start, toCalendarDate(e.start)).toDays(),
  );
  const to = Math.min(
    totalDays,
    Duration.between(start, toCalendarDate(e.end)).toDays() + 1,
  );

  return {
    data: e,
    dimension: {
      min: toDayFraction(totalDays, from),
      max: toDayFraction(totalDays, to),
    },
    lane: { startLane: 0, endLane: 1, maxLane: 1 },
  };
}

function defaultStartDate() {
  return startOfWeek(
    today(getLocalTimeZone()),
    navigator.languages[0],
  ).toString();
}

function isEvent(value: unknown): value is Event {
  return (
    value !== undefined &&
    value !== null &&
    typeof value === 'object' &&
    'key' in value &&
    isString(value.key) &&
    'start' in value &&
    isString(value.start) &&
    'end' in value &&
    isString(value.end) &&
    'subject' in value &&
    isString(value.subject) &&
    (!('fullday' in value) || isBoolean(value.fullday))
  );
}

function eventsFromString(value: string): Events {
  const result = JSON.parse(value);

  if (Array.isArray(result)) {
    return result.filter(isEvent);
  }

  return [];
}

@Component({
  tag: 'qutime-multidayview',
  styleUrl: 'qutime-multidayview.css',
  shadow: true,
})
export class QuTimeMultidayView {
  @Prop()
  startDate: LocalDate = defaultStartDate();

  @Prop()
  days = 7;

  @Prop()
  events: Events | string = [];

  @Prop()
  workingHoursMin = 8;

  @Prop()
  workingHoursMax = 17;

  @Prop()
  hoursMin = 0;

  @Prop()
  hoursMax = 24;

  @State()
  private scrollbarInsets = 0;
  @State()
  private internalDates: CalendarDate[] = [];
  @State()
  private internalEvents: InternalEvent[] = [];
  @State()
  private internalWorkhours: Range<number> = { min: 8, max: 17 };

  private internalHours = [
    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23,
  ];

  contentAreaElement?: HTMLElement;
  resizeObserver?: ResizeObserver;

  scale: SCALE = 'half-hour';

  private observeContentArea() {
    if (
      this.resizeObserver === undefined &&
      this.contentAreaElement !== undefined
    ) {
      const callback: ResizeObserverCallback =
        this.handleContentAreaResized.bind(this);
      this.resizeObserver = new ResizeObserver(callback);
      this.resizeObserver.observe(this.contentAreaElement);
    }
  }

  private handleContentAreaResized() {
    if (this.contentAreaElement) {
      if (
        this.contentAreaElement.offsetWidth !==
        this.contentAreaElement.clientWidth
      ) {
        this.scrollbarInsets =
          this.contentAreaElement.offsetWidth -
          this.contentAreaElement.clientWidth;
      } else {
        this.scrollbarInsets = 0;
      }
    }
  }

  private connectContentArea(el: HTMLElement) {
    this.contentAreaElement = el;
    this.observeContentArea();
  }

  @Watch('startDate')
  @Watch('endDate')
  watchDates() {
    if (this.startDate) {
      let start = parseDate(this.startDate);
      const dates: CalendarDate[] = [];

      for (let i = 0; i < this.days; i++) {
        dates.push(start);
        start = start.add({ days: 1 });
      }

      this.internalDates = dates;
    }
  }

  @Watch('events')
  watchEvents() {
    const events =
      typeof this.events === 'string'
        ? eventsFromString(this.events)
        : this.events;

    this.internalEvents = events.map(e => ({
      key: e.key,
      subject: e.subject,
      fullday: e.fullday,
      start: parseDateTime(e.start),
      end: parseDateTime(e.end),
      type: e.type,
    }));
  }

  @Watch('workingHoursMin')
  @Watch('workingHoursMax')
  watchWorkingHours() {
    const min = Math.max(0, Math.trunc(this.workingHoursMin));
    const max = Math.min(24, Math.trunc(this.workingHoursMax));

    if (min < max) {
      this.internalWorkhours = { min, max };
    }
  }

  @Watch('hoursMin')
  @Watch('hoursMax')
  watchHours() {
    let min = Math.max(0, Math.trunc(this.hoursMin));
    const max = Math.min(24, Math.trunc(this.hoursMax));

    if (min < max) {
      const hours: number[] = [];
      while (min < max) {
        hours.push(min);
        min += 1;
      }
      this.internalHours = hours;
    }
  }

  connectedCallback() {
    this.observeContentArea();
    this.watchDates();
    this.watchEvents();
    this.watchWorkingHours();
    this.watchHours();
  }

  disconnectedCallback() {
    if (this.resizeObserver) {
      this.resizeObserver.disconnect();
      this.resizeObserver = undefined;
    }
  }

  render() {
    if (this.internalDates.length === 0) {
      return <Host>No dates</Host>;
    }
    const startDate = this.internalDates[0];
    const endDate = this.internalDates[this.internalDates.length - 1];
    const dayEntries = this.internalEvents.filter(e => {
      if (e.fullday) {
        const r1 = createDateRange(
          toCalendarDate(e.start),
          toCalendarDate(e.end),
        );
        const r2 = createDateRange(startDate, endDate);
        return intersects(r1, r2, true);
      }
      return false;
    });
    const dayLayout =
      dayEntries.length > 0
        ? computeDayLayout(startDate, this.internalDates.length, dayEntries)
        : undefined;

    return (
      <div
        class="overflow-wrapper"
        style={{
          '--day-count': `${this.internalDates.length}`,
          '--hours-count': `${this.internalHours.length}`,
          '--hour-partition': `${scaleToHourPartition(this.scale)}`,
        }}
      >
        <div class="main-content">
          <div
            class="header-area"
            style={{
              paddingRight: `${this.scrollbarInsets}px`,
            }}
          >
            <div class="days-area">
              <div class="days-area-header-area">
                {this.internalDates.map(d => (
                  <Header date={d} />
                ))}
              </div>
              {dayLayout && dayLayout.entries.length > 0 && (
                <Fragment>
                  <FullDayEvents
                    dates={this.internalDates}
                    layout={dayLayout}
                  ></FullDayEvents>
                </Fragment>
              )}
            </div>
          </div>
          <div class="content-area" ref={this.connectContentArea.bind(this)}>
            <div class="hours-column-area">
              <div class="hours-column-content">
                {this.internalHours.map(hour => (
                  <HourSegment
                    hour={hour}
                    scale={this.scale}
                    showText={true}
                    workhours={this.internalWorkhours}
                    hoursCount={this.internalHours.length}
                  />
                ))}
              </div>
            </div>
            <div class="content-days-area">
              {this.internalDates.map(d => (
                <ContentColumn
                  hours={this.internalHours}
                  workhours={this.internalWorkhours}
                  scale={this.scale}
                  date={d}
                  events={this.internalEvents}
                  hoursCount={this.internalHours.length}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const WEEK_DAY = new DateFormatter('de', { weekday: 'short' });
const MONTH_AND_DAY = new DateFormatter('de', {
  month: 'short',
  day: '2-digit',
});
const DAY = new DateFormatter('de', { day: '2-digit' });
const TIME_FORMAT = new DateFormatter('de', {
  hour: '2-digit',
  minute: '2-digit',
});

const Header = (props: { date: CalendarDate }) => {
  const today = isToday(props.date, getLocalTimeZone());
  const isFirsDayOfMonth = isSameDay(startOfMonth(props.date), props.date);
  const date = props.date.toDate(getLocalTimeZone());

  return (
    <div class="day-header-container">
      <div class={{ 'day-header-text-container': true, today: today }}>
        <div class="day-header-text-node-container">
          <span class="day-text-date">
            {today || isFirsDayOfMonth
              ? MONTH_AND_DAY.format(date).replace(/\./g, '')
              : DAY.format(date)}
          </span>
          <span>{WEEK_DAY.format(date)}</span>
        </div>
      </div>
    </div>
  );
};

type FullDayEventsProps = {
  dates: CalendarDate[];
  layout: LaneLayout<LaneLayoutEntry<InternalEvent>>;
};

const FullDayEvents = (props: FullDayEventsProps) => {
  return (
    <div
      class="header-full-day-container"
      style={{ '--day-max-lanes': `${props.layout.maxLanes}` }}
    >
      {props.dates.map(() => (
        <div class="header-full-day-column"></div>
      ))}
      {props.layout.entries.map(e => (
        <FullDayEntry
          maxLanes={props.layout.maxLanes}
          startDate={props.dates[0]}
          endDate={props.dates[props.dates.length - 1]}
          layoutEntry={e}
        ></FullDayEntry>
      ))}
    </div>
  );
};

type FullDayEntryProps = {
  startDate: CalendarDate;
  endDate: CalendarDate;
  maxLanes: number;
  layoutEntry: LaneLayoutEntry<InternalEvent>;
};

const FullDayEntry = (props: FullDayEntryProps) => {
  const top = (props.layoutEntry.lane.startLane / props.maxLanes) * 100 + '%';
  const bottom =
    'calc(' +
    (100 - ((props.layoutEntry.lane.startLane + 1) / props.maxLanes) * 100) +
    '% + 2px)';
  const left = 'calc(' + props.layoutEntry.dimension.min + '% + 4px)';
  const right = 'calc(' + (100 - props.layoutEntry.dimension.max) + '% + 3px)';

  const event = props.layoutEntry.data;
  const start = toCalendarDate(event.start);
  const end = toCalendarDate(event.end);
  const leadText =
    props.startDate.compare(start) > 0
      ? MONTH_AND_DAY.format(start.toDate(getLocalTimeZone())).replace(
          /\./g,
          '',
        )
      : undefined;
  const endText =
    props.endDate.compare(end) < 0
      ? MONTH_AND_DAY.format(end.toDate(getLocalTimeZone())).replace(/\./g, '')
      : undefined;

  const chevronLeft = (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 320 512"
      aria-hidden="true"
      style={{ height: '1em', verticalAlign: '-0.125em' }}
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
      style={{ height: '1em', verticalAlign: '-0.125em' }}
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

  return (
    <div class="day-event" tabIndex={0} style={{ top, bottom, left, right }}>
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

type ContentColumnProps = {
  date: CalendarDate;
  events: readonly InternalEvent[];
  hours: number[];
  scale: SCALE;
  workhours: Range<number>;
  hoursCount: number;
};

const ContentColumn = (props: ContentColumnProps) => {
  const layout = computeTimeLayout(
    props.date,
    props.events.filter(e => {
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
        {props.hours.map(hour => (
          <HourSegment
            hour={hour}
            scale={props.scale}
            showText={false}
            workhours={props.workhours}
            hoursCount={props.hoursCount}
          />
        ))}
        {layout.entries.map(entry => (
          <TimeEventElement entry={entry} startShift={startShift} />
        ))}
      </div>
    </div>
  );
};

const TimeEventElement = (props: {
  entry: LaneLayoutEntry<InternalEvent>;
  startShift: number;
}) => {
  const { entry } = props;
  const event = entry.data;
  const top = `calc(${entry.dimension.min - props.startShift}% + 2px)`;
  const bottom = `calc(${Math.max(0, 100 - entry.dimension.max + props.startShift)}% + 1px)`;
  const left = `${(entry.lane.startLane / entry.lane.maxLane) * 100}%`;
  const right = `calc(max(${100 - (entry.lane.endLane / entry.lane.maxLane) * 100}% + 2px, 10px))`;

  const timezone = getLocalTimeZone();
  const startTime = TIME_FORMAT.format(event.start.toDate(timezone));
  const endTime = TIME_FORMAT.format(event.end.toDate(timezone));

  const part = event.type ? `time-event ${event.type}` : 'time-event';

  const styleClasses = {
    'time-event': true,
  };

  if (event.type) {
    styleClasses[event.type] = true;
  }

  return (
    <div
      class={styleClasses}
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
};

type HourSegmentProps = {
  hour: number;
  scale: SCALE;
  showText: boolean;
  workhours: Range<number>;
  hoursCount: number;
};

const HourSegment = (props: HourSegmentProps) => {
  const hourText = props.showText ? (
    <div class="hour-text-positioner">
      <span class="hour-text-text">
        {props.hour < 10 ? '0' + props.hour + ':00' : props.hour + ':00'}
      </span>
    </div>
  ) : undefined;

  return (
    <Fragment>
      <div
        class={{
          'hour-segment': true,
          'hour-segment-work-hour':
            props.hour >= props.workhours.min &&
            props.hour < props.workhours.max,
          'hour-segment-dashed': true,
          'hour-segment-top-none-working':
            props.hour < props.workhours.min + 1 ||
            props.hour > props.workhours.max - 1,
          'hour-segment-bottom-none-working':
            props.hour < props.workhours.min ||
            props.hour > props.workhours.max - 1,
        }}
      >
        {hourText}
      </div>
      <div
        class={{
          'hour-segment-work-hour':
            props.hour >= props.workhours.min &&
            props.hour < props.workhours.max,
        }}
      ></div>
    </Fragment>
  );
};
