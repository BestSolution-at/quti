import {
  CalendarDate,
  DateFormatter,
  getLocalTimeZone,
  isSameDay,
  parseDate,
  parseDateTime,
  parseTime,
  startOfWeek,
  Time,
  toCalendarDate,
  today,
  toTime,
} from "@internationalized/date";
import { InternalEvent, QEvent, SCALE } from "./types";

export type Range<T> = {
  min: T;
  max: T;
  readonly comparator?: (a: T, b: T) => number;
};

export function intersects<T>(a: Range<T>, b: Range<T>, inclusive = false) {
  if (inclusive) {
    if (a.comparator) {
      return a.comparator(a.max, b.min) >= 0 && a.comparator(a.min, b.max) <= 0;
    }
    return a.max >= b.min && a.min <= b.max;
  }

  if (a.comparator) {
    return a.comparator(a.max, b.min) > 0 && a.comparator(a.min, b.max) < 0;
  }
  return a.max > b.min && a.min < b.max;
}

const MILLIS_PER_SECOND = 1000;
const MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
const MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
const MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

export class Duration {
  constructor(private readonly milliSeconds: number) {}

  public static between(start: CalendarDate, end: CalendarDate) {
    const a = start.toDate(getLocalTimeZone());
    const b = end.toDate(getLocalTimeZone());

    return new Duration(
      Date.UTC(b.getFullYear(), b.getMonth(), b.getDate()) -
        Date.UTC(a.getFullYear(), a.getMonth(), a.getDate()),
    );
  }

  public toDays(): number {
    return Math.floor(this.milliSeconds / MILLIS_PER_DAY);
  }
}

export function createDateRange(
  min: CalendarDate,
  max: CalendarDate,
): Range<CalendarDate> {
  return {
    min,
    max,
    comparator: (a, b) => a.compare(b),
  };
}

export type LaneLayoutEntry<T> = {
  readonly data: T;
  readonly dimension: { readonly min: number; readonly max: number };
  readonly lane: { startLane: number; endLane: number; maxLane: number };
};

export type LaneLayout<T> = {
  get maxLanes(): number;
  readonly entries: readonly T[];
};

export function computeLaneLayout<T extends LaneLayoutEntry<unknown>>(
  layoutEntries: T[],
): LaneLayout<T> {
  let lanes: LaneLayoutEntry<unknown>[][] = [];
  let lastEnding: number | null = null;

  const rv: T[] = layoutEntries.sort((e1, e2) => {
    const rv = e1.dimension.min - e2.dimension.min;
    if (rv !== 0) {
      return rv;
    }
    return e1.dimension.max - e2.dimension.max;
  });

  rv.forEach((e) => {
    if (lastEnding !== null && e.dimension.min >= lastEnding) {
      computeLanes(lanes);
      lanes = [];
      lastEnding = null;
    }

    let placed = false;
    // eslint-disable-next-line @typescript-eslint/prefer-for-of
    for (let i = 0; i < lanes.length; i++) {
      const col = lanes[i];
      if (!intersects(col[col.length - 1].dimension, e.dimension)) {
        col.push(e);
        placed = true;
        break;
      }
    }

    if (!placed) {
      lanes.push([e]);
    }

    if (lastEnding === null || e.dimension.max > lastEnding) {
      lastEnding = e.dimension.max;
    }
  });

  if (lanes.length > 0) {
    computeLanes(lanes);
  }

  return {
    get maxLanes() {
      return rv
        .map((e) => e.lane.maxLane)
        .reduce((a, b) => Math.max(a, b), -Infinity);
    },
    entries: rv,
  };
}

function computeLanes(lanes: LaneLayoutEntry<unknown>[][]) {
  lanes.forEach((lane, index) => {
    lane.forEach((layoutEntry) => {
      const colSpan = computeLaneSpan(layoutEntry, index, lanes);
      layoutEntry.lane.startLane = index;
      layoutEntry.lane.endLane = index + colSpan;
      layoutEntry.lane.maxLane = lanes.length;
    });
  });
}

function computeLaneSpan(
  layoutEntry: LaneLayoutEntry<unknown>,
  laneIndex: number,
  columns: LaneLayoutEntry<unknown>[][],
) {
  let colSpan = 1;
  for (let i = laneIndex + 1; i < columns.length; i++) {
    const col = columns[i];
    for (const e of col) {
      if (intersects(layoutEntry.dimension, e.dimension)) {
        return colSpan;
      }
    }
    colSpan++;
  }
  return colSpan;
}

export function isString(value: unknown): value is string {
  return typeof value === "string";
}

export function isBoolean(value: unknown): value is boolean {
  return typeof value === "boolean";
}

export function isRecord(value: unknown): value is Record<string, unknown> {
  return value !== null && value !== undefined && typeof value === "object";
}

export function isNumber(value: unknown): value is number {
  return typeof value === "number";
}

export function isError(value: unknown): value is { error: string } {
  return isRecord(value) && "error" in value && isString(value.error);
}

export function isDefined<T>(value: T | undefined): value is T {
  return value !== undefined;
}

type Error = { error: string };

export type Result<T> = T | Error;

export function checkISODate(
  value: string | undefined,
  propertyName: string,
): Result<CalendarDate> {
  if (value === undefined) {
    return { error: `Required attribute ${propertyName} is not defined` };
  }
  try {
    return parseDate(value);
  } catch (e) {
    return {
      error: `Attribute ${propertyName} is not an ISO-Date: ${String(e)}`,
    };
  }
}

export function checkInteger(
  value: number | undefined,
  propertyName: string,
  min: number,
): Result<number> {
  if (value === undefined) {
    return { error: `Required attribute ${propertyName} is not defined` };
  }
  if (value < min) {
    return { error: `Attribute ${propertyName} has to be larger than ${min}` };
  }
  if (Math.round(value) !== value) {
    return { error: `Attribute ${propertyName} has to be an integer value` };
  }
  return value;
}

export function checkIntegerMinMax(
  value: number | undefined,
  propertyName: string,
  min: number,
  max: number,
): Result<number> {
  if (value === undefined) {
    return { error: `Required attribute ${propertyName} is not defined` };
  }
  if (value < min) {
    return { error: `Attribute ${propertyName} has to be larger than ${min}` };
  }
  if (value > max) {
    return { error: `Attribute ${propertyName} has to be smaller than ${max}` };
  }
  if (Math.round(value) !== value) {
    return { error: `Attribute ${propertyName} has to be an integer value` };
  }
  return value;
}

export type AllNonErrors<T> = {
  [P in keyof T]: Exclude<T[P], Error>;
};

export function allErrors(value: Record<string, unknown>): Error[] {
  return Object.keys(value)
    .map((k) => {
      const v = value[k];
      if (isError(v)) {
        return v;
      } else {
        return undefined;
      }
    })
    .filter(isDefined);
}

export function checkAllPropsNonError<T extends object>(
  value: T,
): value is AllNonErrors<T> {
  return (
    isRecord(value) &&
    Object.keys(value).findIndex((k) => isError(value[k])) === -1
  );
}

export function scaleToHourPartition(scale: SCALE): number {
  switch (scale) {
    case "10-min":
      return 6;
    case "5-min":
      return 12;
    case "6-min":
      return 10;
    case "half-hour":
      return 2;
    case "quarter-hour":
      return 4;
  }
  return 1;
}

export function toTimeFraction(time: Time, hoursCount: number) {
  const hour = 100 / hoursCount;
  const minutes = hour / 60;
  return hour * time.hour + minutes * time.minute;
}

function toTimeLayoutEntry(
  date: CalendarDate,
  e: InternalEvent,
  hoursCount: number,
): LaneLayoutEntry<InternalEvent> {
  const from = isSameDay(date, e.start) ? toTime(e.start) : parseTime("00:00");
  const to = isSameDay(date, e.end) ? toTime(e.end) : parseTime("23:59"); //FIXME
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

export function computeTimeLayout(
  date: CalendarDate,
  entries: readonly InternalEvent[],
  hoursCount: number,
): LaneLayout<LaneLayoutEntry<InternalEvent>> {
  return computeLaneLayout(
    entries.map((event) => toTimeLayoutEntry(date, event, hoursCount)),
  );
}

export function computeDayLayout(
  startDate: CalendarDate,
  days: number,
  entries: readonly InternalEvent[],
): LaneLayout<LaneLayoutEntry<InternalEvent>> {
  return computeLaneLayout(
    entries.map((event) => toDayLayoutEntry(startDate, days, event)),
  );
}

function toDayLayoutEntry(
  start: CalendarDate,
  totalDays: number,
  e: InternalEvent,
): LaneLayoutEntry<InternalEvent> {
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

export function defaultStartDate() {
  return startOfWeek(
    today(getLocalTimeZone()),
    navigator.languages[0],
  ).toString();
}

export function isEvent(value: unknown): value is QEvent {
  const result =
    value !== undefined &&
    value !== null &&
    typeof value === "object" &&
    "key" in value &&
    isString(value.key) &&
    "start" in value &&
    isString(value.start) &&
    "end" in value &&
    isString(value.end) &&
    "subject" in value &&
    isString(value.subject) &&
    (!("fullday" in value) || isBoolean(value.fullday)) &&
    (!("type" in value) || isString(value.type));
  if (!result) {
    console.warn("Not at valid event:", value);
  }
  return result;
}

export function computeDates(start: CalendarDate, days: number) {
  const dates: CalendarDate[] = [];

  for (let i = 0; i < days; i++) {
    dates.push(start);
    start = start.add({ days: 1 });
  }
  return dates;
}

export function checkScale(value: string | undefined): Result<SCALE> {
  if (value === undefined) {
    return { error: 'Required attribute "scale" is not defined' };
  }
  switch (value) {
    case "hour":
      return value;
    case "half-hour":
      return value;
    case "quarter-hour":
      return value;
    case "10-min":
      return value;
    case "6-min":
      return value;
    case "5-min":
      return value;
  }
  return {
    error:
      'Attribute "scale" has to be "hour", "half-hour", "quarter-hour", "10-min", "6-min" or "5-min"',
  };
}

export function classesString(o: Record<string, boolean>) {
  return Object.keys(o)
    .filter((k) => o[k])
    .join(" ");
}

export function mapToInternalEvent(e: QEvent): InternalEvent {
  return {
    key: e.key,
    subject: e.subject,
    fullday: e.fullday,
    start: parseDateTime(e.start),
    end: parseDateTime(e.end),
    type: e.type,
    originalEvent: e,
  };
}

export function createDayRangeFilter(
  startDate: CalendarDate,
  endDate: CalendarDate,
) {
  const r2 = createDateRange(startDate, endDate);
  return (e: InternalEvent) => {
    if (e.fullday) {
      const r1 = createDateRange(
        toCalendarDate(e.start),
        toCalendarDate(e.end),
      );

      return intersects(r1, r2, true);
    }
    return false;
  };
}

export function computeInternalHours(min: number, max: number) {
  if (min < max) {
    const hours: number[] = [];
    while (min < max) {
      hours.push(min);
      min += 1;
    }
    return hours;
  }
  return [
    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23,
  ];
}

export const WEEK_DAY = new DateFormatter("de", { weekday: "short" });
export const MONTH_AND_DAY = new DateFormatter("de", {
  month: "short",
  day: "2-digit",
});
export const DAY = new DateFormatter("de", { day: "2-digit" });
export const TIME_FORMAT = new DateFormatter("de", {
  hour: "2-digit",
  minute: "2-digit",
});
