import { CalendarDate, getLocalTimeZone } from '@internationalized/date';

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

  rv.forEach(e => {
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
        .map(e => e.lane.maxLane)
        .reduce((a, b) => Math.max(a, b), -Infinity);
    },
    entries: rv,
  };
}

function computeLanes(lanes: LaneLayoutEntry<unknown>[][]) {
  lanes.forEach((lane, index) => {
    lane.forEach(layoutEntry => {
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
  return typeof value === 'string';
}

export function isBoolean(value: unknown): value is boolean {
  return typeof value === 'boolean';
}
