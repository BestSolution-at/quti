import {
  Component,
  Fragment,
  Host,
  Prop,
  State,
  Watch,
  h
} from "@stencil/core";

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
  toCalendarDate,
  toTime
} from "@internationalized/date";

import {
  Duration,
  LaneLayout,
  LaneLayoutEntry,
  Range,
  computeLaneLayout,
  createDateRange,
  intersects
} from "../../utils/utils";

export type LocalDate = string
export type LocalDatetime = string;

export type LocalDates = readonly LocalDate[];

export type Event = {
  readonly key: string
  readonly start: LocalDatetime
  readonly end: LocalDatetime
  readonly subject: string
  readonly fullday?: boolean
}

export type Events = readonly Event[];

type InternalEvent = {
  readonly key: string
  readonly start: CalendarDateTime
  readonly end: CalendarDateTime
  readonly subject: string
  readonly fullday?: boolean
}

export type SCALE = 'hour' | 'half-hour' | 'quarter-hour' | '10-min' | '6-min' | '5-min';

function scaleToMinSize(scale: SCALE, hoursCount: number) {
  const total = hoursCount * 30;

  if( scale === 'hour' ) {
    return total;
  } else if( scale === 'quarter-hour' ) {
    return total * 4;
  } else if( scale === '10-min' ) {
    return total * 6;
  } else if( scale === '6-min' ) {
    return total * 10;
  } else if( scale === '5-min' ) {
    return total * 12;
  }
  return total * 2;
}

function toTimeFraction(time: Time, hoursCount: number) {
  const hour = 100 / hoursCount;
  const minutes = hour / 60;
  return hour * time.hour + minutes * time.minute;
}

function toTimeLayoutEntry(date: CalendarDate, e: InternalEvent, hoursCount: number): LaneLayoutEntry<InternalEvent> {
  const from = isSameDay(date, e.start) ? toTime(e.start) : parseTime('00:00');
  const to = isSameDay(date, e.end) ? toTime(e.end) : parseTime('23:59'); //FIXME
  return {
    data: e,
    dimension: { min: toTimeFraction(from, hoursCount), max: toTimeFraction(to, hoursCount) },
    lane: { startLane: 0, endLane: 1, maxLane: 1 }
  };
}

function toDayFraction(totalDays: number, day: number) {
  return (100 / totalDays) * day;
}

function computeTimeLayout(date: CalendarDate, entries: readonly InternalEvent[], hoursCount: number): LaneLayout<LaneLayoutEntry<InternalEvent>> {
  return computeLaneLayout(entries.map( event => toTimeLayoutEntry(date, event, hoursCount)));
}

function computeDayLayout(startDate: CalendarDate, days: number, entries: readonly InternalEvent[]): LaneLayout<LaneLayoutEntry<InternalEvent>> {
  return computeLaneLayout(entries.map( event => toDayLayoutEntry(startDate, days, event)));
}

function toDayLayoutEntry(start: CalendarDate, totalDays: number, e: InternalEvent): LaneLayoutEntry<InternalEvent> {
  parseDuration
  const from = Math.max(0, Duration.between(start, toCalendarDate(e.start)).toDays());
  const to = Math.min(totalDays, Duration.between(start, toCalendarDate(e.end)).toDays() + 1);

  return {
    data: e,
    dimension: { min: toDayFraction(totalDays, from), max: toDayFraction(totalDays, to) },
    lane: { startLane: 0, endLane: 1, maxLane: 1 },
  };
}

@Component({
  tag: 'qutime-multidayview',
  styleUrl: 'qutime-multidayview.css',
  shadow: true
})
export class QuTimeMultidayView {
  @Prop()
  dates: LocalDates;

  @Prop()
  events: Events;

  @Prop()
  workingHoursMin: number = 8;

  @Prop()
  workingHoursMax: number = 17;

  @Prop()
  hoursMin: number = 0;

  @Prop()
  hoursMax: number = 24;

  @State()
  private scrollbarInsets: number = 0;
  @State()
  private internalDates: CalendarDate[] = [];
  @State()
  private internalEvents: InternalEvent[] = [];
  @State()
  private internalWorkhours: Range<number> = { min: 8, max: 17 };

  private internalHours = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 ];

  contentAreaElement?: HTMLElement;
  resizeObserver?: ResizeObserver;

  scale: SCALE = 'half-hour';

  private observeContentArea() {
    if( this.resizeObserver === undefined && this.contentAreaElement !== undefined ) {
      this.resizeObserver = new ResizeObserver(this.handleContentAreaResized.bind(this));
      this.resizeObserver.observe(this.contentAreaElement);
    }
  }

  private handleContentAreaResized() {
    if( this.contentAreaElement ) {
      if( this.contentAreaElement.offsetWidth !== this.contentAreaElement.clientWidth ) {
        this.scrollbarInsets = this.contentAreaElement.offsetWidth - this.contentAreaElement.clientWidth
      } else {
        this.scrollbarInsets = 0;
      }
    }
  }

  private connectContentArea(el: HTMLElement) {
    this.contentAreaElement = el;
    this.observeContentArea();
  }

  @Watch("dates")
  watchDates(dates: readonly LocalDate[]) {
    this.internalDates = dates.map( parseDate )
    console.log('this', this.internalDates);
  }

  @Watch("events")
  watchEvents(events: readonly Event[]) {
    this.internalEvents = events.map( e => ({
      key: e.key,
      subject: e.subject,
      fullday: e.fullday,
      start: parseDateTime(e.start),
      end: parseDateTime(e.end)
    }));
  }

  @Watch("workingHoursMin")
  @Watch("workingHoursMax")
  watchWorkingHours() {
    const min = Math.max(0, this.workingHoursMin);
    const max = Math.min(24, this.workingHoursMax);

    if( min < max ) {
      this.internalWorkhours = { min, max }
    }
  }

  @Watch("hoursMin")
  @Watch("hoursMax")
  watchHours() {
    let min = Math.max(0, this.hoursMin);
    const max = Math.min(24, this.hoursMax);

    if( min < max ) {
      const hours = [];
      while( min < max ) {
        hours.push( min );
        min += 1;
      }
      this.internalHours = hours;
    }
  }

  connectedCallback() {
    this.observeContentArea();
    this.watchDates(this.dates);
    this.watchEvents(this.events);
    this.watchWorkingHours();
    this.watchHours();
  }

  disconnectedCallback() {
    if( this.resizeObserver ) {
      this.resizeObserver.disconnect();
      this.resizeObserver = undefined;
    }
  }

  render() {
    const startDate = this.internalDates[0];
    const endDate = this.internalDates[this.internalDates.length - 1];
    const dayEntries = this.internalEvents.filter( e => {
      if( e.fullday ) {
        const r1 = createDateRange( toCalendarDate(e.start), toCalendarDate(e.end) );
        const r2 = createDateRange( startDate, endDate );
        return intersects(r1, r2, true);
      }
      return false;
    } );
    const dayLayout = startDate && dayEntries && dayEntries.length > 0 ? computeDayLayout(startDate, this.internalDates.length, dayEntries) : undefined;

    return <Host style={{ '--day-count': `${this.dates.length}` }}>
      <div class="header-area" style={{ paddingRight: `${this.scrollbarInsets}px` }}>
        <div class="days-area">
          <div class="days-area-header-area">
            { this.internalDates.map( d => <Header date={d} /> ) }
          </div>
          { dayLayout && dayLayout.entries.length > 0 &&
            <Fragment>
              <FullDayEvents dates={this.internalDates} layout={dayLayout}></FullDayEvents>
            </Fragment>
          }
        </div>
      </div>
      <div class="content-area" ref={this.connectContentArea.bind(this)}>
        <div style={{ display: 'flex' }}>
          <div class="hours-column-area">
            <div class="content-top-spacer"></div>
            <div style={{ minHeight: `${scaleToMinSize(this.scale, this.internalHours.length)}px`, width: 'var(--size-600)' }}>
              { this.internalHours.map( hour =>
                <HourSegment
                  hour={hour}
                  scale={this.scale}
                  showText={true}
                  workhours={this.internalWorkhours}
                  hoursCount={this.internalHours.length}
                /> ) }
            </div>
          </div>
          { this.internalDates.map( d =>
            <ContentColumn
              base={100/this.internalDates.length}
              hours={this.internalHours}
              workhours={this.internalWorkhours} scale={this.scale}
              date={d}
              events={this.internalEvents}
              hoursCount={this.internalHours.length}
            /> ) }
        </div>
      </div>
    </Host>
  }
}

const WEEK_DAY = new DateFormatter('de', { weekday: 'short' });
const MONTH_AND_DAY = new DateFormatter( 'de', { month: 'short', day: '2-digit',  } );
const DAY = new DateFormatter( 'de', { day: '2-digit',  } );
const TIME_FORMAT = new DateFormatter('de', { hour: '2-digit', minute: '2-digit' });

const Header = (props: { date: CalendarDate } ) => {
  const today = isToday(props.date, getLocalTimeZone());
  const isFirsDayOfMonth = isSameDay(startOfMonth(props.date), props.date);
  const date = props.date.toDate(getLocalTimeZone());

  return <div class="day-header-container">
    <div class={{"day-header-text-container": true, "today": today}}>
      <div class="day-header-text-node-container">
        <span class='day-text-date'>{ today || isFirsDayOfMonth ? MONTH_AND_DAY.format(date).replace(/\./g, '') : DAY.format(date) }</span>
        <span>{WEEK_DAY.format(date)}</span>
      </div>
    </div>
  </div>
};

type FullDayEventsProps = {
  dates: CalendarDate[],
  layout: LaneLayout<LaneLayoutEntry<InternalEvent>>
}

const FullDayEvents = (props: FullDayEventsProps) => {
  const height = 'calc( ' + props.layout.maxLanes + ' * var(--size-300)';
  return <div class="header-full-day-container">
    { props.dates.map( () =>
      <div class="header-full-day-column">
        <div style={{ minHeight: `${height}` }}></div>
      </div> ) }
      { props.layout.entries.map( e => <FullDayEntry maxLanes={props.layout.maxLanes} startDate={props.dates[0]} endDate={props.dates[props.dates.length - 1]} layoutEntry={e}></FullDayEntry> ) }
  </div>
}

type FullDayEntryProps = {
  startDate: CalendarDate,
  endDate: CalendarDate,
  maxLanes: number,
  layoutEntry: LaneLayoutEntry<InternalEvent>
}

const FullDayEntry = (props: FullDayEntryProps) => {
  const top = (props.layoutEntry.lane.startLane / props.maxLanes) * 100 + '%';
  const bottom = 'calc(' + (100 - ((props.layoutEntry.lane.startLane + 1) / props.maxLanes) * 100) + '% + 2px)';
  const left = 'calc(' + props.layoutEntry.dimension.min + '% + 4px)';
  const right = 'calc(' + (100 - props.layoutEntry.dimension.max) + '% + 3px)';

  const event = props.layoutEntry.data;
  const start = toCalendarDate(event.start);
  const end = toCalendarDate(event.end);
  const leadText = props.startDate.compare(start) > 0 ? MONTH_AND_DAY.format(start.toDate(getLocalTimeZone())).replace(/\./g, '') : undefined;
  const endText = props.endDate.compare(end) < 0 ? MONTH_AND_DAY.format(end.toDate(getLocalTimeZone())).replace(/\./g, '') : undefined;

  const chevronLeft = <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512" aria-hidden="true" style={ { height: '1em', verticalAlign: '-0.125em' } }>
    <description>
    Font Awesome Free 6.5.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.
    </description>
    <path fill="currentColor" d="M9.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l192 192c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L77.3 256 246.6 86.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-192 192z"/>
  </svg>

  const chevronRight = <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512" aria-hidden="true" style={ { height: '1em', verticalAlign: '-0.125em' } }>
    <description>
      Font Awesome Free 6.5.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.
    </description>
    <path fill="currentColor" d="M310.6 233.4c12.5 12.5 12.5 32.8 0 45.3l-192 192c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3L242.7 256 73.4 86.6c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0l192 192z"/>
  </svg>

  return <div class="day-event"
    tabIndex={0}
    style={{ top, bottom, left, right }}>
    { /* Create a new stacking context so that we can use :after/:before */ }
    <div class="day-event-text-container">
      { leadText && <span class="day-event-text-lead-text">{chevronLeft} {leadText}</span> }
      <span class="day-event-text">{props.layoutEntry.data.subject}</span>
      { endText && <span class="day-event-text-end-text">{endText} {chevronRight}</span> }
    </div>
  </div>
}

type ContentColumnProps = {
  date: CalendarDate,
  events: readonly InternalEvent[],
  base: number,
  hours: number[],
  scale: SCALE,
  workhours: Range<number>
  hoursCount: number
}

const ContentColumn = ( props: ContentColumnProps ) => {
  console.log('Events', props.events)
  const layout = computeTimeLayout(props.date,  props.events.filter( e => {
    return e.fullday !== true && (isSameDay(toCalendarDate(e.start), props.date) || isSameDay(toCalendarDate(e.end), props.date));
  }), props.hoursCount)
  return <div class="content-column" style={{
    flexBasis: `${props.base}%`,
  }}>
    <div class="content-column-content">
      <div class="content-top-spacer"></div>
      <div style={{ minHeight: `${scaleToMinSize(props.scale, props.hoursCount)}px`, position: 'relative' }}>
        { props.hours.map( hour =>
          <HourSegment
            hour={hour}
            scale={props.scale}
            showText={false}
            workhours={props.workhours}
            hoursCount={props.hoursCount}
            />)
        }
        { layout.entries.map( entry => <TimeEventElement entry={entry} /> ) }
      </div>
    </div>
  </div>
};

const TimeEventElement = (props: { entry: LaneLayoutEntry<InternalEvent>}) => {
  const { entry } = props;
  const event = entry.data;
  const top = 'calc(' + entry.dimension.min + '% + 2px)';
  const bottom = 'calc(' + (100 - entry.dimension.max) + '% + 1px)';
  const left = (entry.lane.startLane / entry.lane.maxLane) * 100 + '%';
  const right = 'calc(max(' + ( 100 - (entry.lane.endLane / entry.lane.maxLane) * 100) + '% + 2px, 10px))';

  const timezone = getLocalTimeZone();
  const startTime = TIME_FORMAT.format(event.start.toDate(timezone));
  const endTime = TIME_FORMAT.format(event.end.toDate(timezone));

  return <div class="time-event" tabIndex={0} style={{
    left,
    right,
    top,
    bottom,
  }}>
    <div class="time-event-content">
      <div class="event-text-positioner">
        <div class="event-text">
          <span class="subject">{event.subject}</span>
          <div class="time">{startTime} - {endTime}</div>
        </div>
      </div>
    </div>
  </div>
}

type HourSegmentProps = {
  hour: number,
  scale: SCALE,
  showText: boolean,
  workhours: Range<number>
  hoursCount: number
}

const HourSegment = ( props: HourSegmentProps ) => {
  const segment = 100 / props.hoursCount;
  // const top = segment * props.hour;

  const borderTopColor = props.hour < props.workhours.min + 1 || props.hour > props.workhours.max - 1 ? 'gray-400' : 'gray-300';
  const borderBottomColor = props.hour < props.workhours.min || props.hour > props.workhours.max - 1 ? 'gray-400' : 'gray-300';
  const background = props.hour >= props.workhours.min && props.hour < props.workhours.max ? 'gray-50' : undefined

  const hourText = props.showText ? <div class="hour-text-positioner">
      <span class="hour-text-text">{ props.hour < 10 ? '0'+props.hour+':00' : props.hour + ':00' }</span>
    </div>
  : undefined;

  if( props.scale === 'hour' ) {

  }

  return <Fragment>
    <div class="hour-text-container-dashed" style={{
      height: segment / 2 + '%',

      borderTopColor: `var(--color-${borderTopColor})`,
      borderBottomColor: `var(--color-${borderBottomColor})`,
      backgroundColor: background ? `var(--color-${background})` : undefined
      }}>
      {hourText}
    </div>
    <div style={{
      height: segment / 2 + '%',
      backgroundColor: background ? `var(--color-${background})` : undefined
    }}>
    </div>
  </Fragment>
};
