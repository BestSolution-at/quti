import {
	CalendarDate,
	DateFormatter,
	getLocalTimeZone,
	isSameDay,
	parseTime,
	Time,
	toCalendarDate,
	toTime,
} from '@internationalized/date';

import {
	COMPACTNESS,
	QutiDayEvent,
	QutiTimeEvent,
	Range,
	SCALE,
} from './View.types';

export interface LaneLayoutEntry<T> {
	readonly data: T;
	readonly dimension: { readonly min: number; readonly max: number };
	readonly lane: { startLane: number; endLane: number; maxLane: number };
}

export interface LaneLayout<T> {
	get maxLanes(): number;
	readonly entries: readonly T[];
}

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
		for (let j = 0; j < col.length; j++) {
			const e = col[j];
			if (intersects(layoutEntry.dimension, e.dimension)) {
				return colSpan;
			}
		}
		colSpan++;
	}
	return colSpan;
}

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

const ALL_HOURS = [
	0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
	22, 23,
];

export function hoursFromRange(range?: Range<number>) {
	if (range) {
		let min = Math.max(0, Math.trunc(range.min));
		const max = Math.min(24, Math.trunc(range.max));

		if (min < max) {
			const hours: number[] = [];
			while (min < max) {
				hours.push(min);
				min += 1;
			}
			console.log(hours);
			return hours;
		}
	}
	return ALL_HOURS;
}

export function computeDayLayout(
	startDate: CalendarDate,
	days: number,
	entries: readonly QutiDayEvent[],
): LaneLayout<LaneLayoutEntry<QutiDayEvent>> {
	return computeLaneLayout(
		entries.map((event) => toDayLayoutEntry(startDate, days, event)),
	);
}

function toDayLayoutEntry(
	start: CalendarDate,
	totalDays: number,
	e: QutiDayEvent,
): LaneLayoutEntry<QutiDayEvent> {
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

function toDayFraction(totalDays: number, day: number) {
	return (100 / totalDays) * day;
}

export const condensedFormat = new DateFormatter('de', {
	weekday: 'long',
	day: '2-digit',
	month: '2-digit',
});

export const WEEK_DAY = new DateFormatter('de', { weekday: 'short' });
export const MONTH_AND_DAY = new DateFormatter('de', {
	month: 'short',
	day: '2-digit',
});
export const DAY = new DateFormatter('de', { day: '2-digit' });
export const TIME_FORMAT = new DateFormatter('de', {
	hour: '2-digit',
	minute: '2-digit',
});

export function scaleToMinSize(
	scale: SCALE,
	hoursCount: number,
	compactness: COMPACTNESS,
) {
	let segmentHeight = 30;

	if (compactness === 'compact') {
		segmentHeight = 15;
	} else if (compactness === 'medium') {
		segmentHeight = 25;
	}

	if (scale === 'hour') {
		return segmentHeight * hoursCount;
	} else if (scale === 'quarter-hour') {
		return segmentHeight * hoursCount * 4;
	} else if (scale === '10-min') {
		return segmentHeight * hoursCount * 6;
	} else if (scale === '6-min') {
		return segmentHeight * hoursCount * 10;
	} else if (scale === '5-min') {
		return segmentHeight * hoursCount * 12;
	}
	return segmentHeight * hoursCount * 2;
}

export function computeBottoms(
	hour: number,
	segment: number,
	scale: 'quarter-hour' | '10-min' | '6-min' | '5-min',
) {
	if (scale === 'quarter-hour') {
		const subsegment = segment / 4;
		return [
			100 - (segment * hour + subsegment),
			100 - (segment * hour + 2 * subsegment),
			100 - (segment * hour + 3 * subsegment),
		];
	} else if (scale === '10-min') {
		const subsegment = segment / 6;
		return [
			100 - (segment * hour + subsegment),
			100 - (segment * hour + 2 * subsegment),
			100 - (segment * hour + 3 * subsegment),
			100 - (segment * hour + 4 * subsegment),
			100 - (segment * hour + 5 * subsegment),
		];
	} else if (scale === '6-min') {
		const subsegment = segment / 10;
		return [
			100 - (segment * hour + subsegment),
			100 - (segment * hour + 2 * subsegment),
			100 - (segment * hour + 3 * subsegment),
			100 - (segment * hour + 4 * subsegment),
			100 - (segment * hour + 5 * subsegment),
			100 - (segment * hour + 6 * subsegment),
			100 - (segment * hour + 7 * subsegment),
			100 - (segment * hour + 8 * subsegment),
			100 - (segment * hour + 9 * subsegment),
		];
	}

	const subsegment = segment / 12;
	return [
		100 - (segment * hour + subsegment),
		100 - (segment * hour + 2 * subsegment),
		100 - (segment * hour + 3 * subsegment),
		100 - (segment * hour + 4 * subsegment),
		100 - (segment * hour + 5 * subsegment),
		100 - (segment * hour + 6 * subsegment),
		100 - (segment * hour + 7 * subsegment),
		100 - (segment * hour + 8 * subsegment),
		100 - (segment * hour + 9 * subsegment),
		100 - (segment * hour + 10 * subsegment),
		100 - (segment * hour + 11 * subsegment),
	];
}

export function computeTimeLayout(
	date: CalendarDate,
	entries: readonly QutiTimeEvent[],
	hoursCount: number,
): LaneLayout<LaneLayoutEntry<QutiTimeEvent>> {
	return computeLaneLayout(
		entries.map((event) => toTimeLayoutEntry(date, event, hoursCount)),
	);
}

function toTimeLayoutEntry(
	date: CalendarDate,
	e: QutiTimeEvent,
	hoursCount: number,
): LaneLayoutEntry<QutiTimeEvent> {
	const from = isSameDay(date, e.start) ? toTime(e.start) : parseTime('00:00');
	const to = isSameDay(date, e.end) ? toTime(e.end) : parseTime('24:00');
	return {
		data: e,
		dimension: {
			min: toTimeFraction(from, hoursCount),
			max: toTimeFraction(to, hoursCount),
		},
		lane: { startLane: 0, endLane: 1, maxLane: 1 },
	};
}

export function toTimeFraction(time: Time, hoursCount: number) {
	const hour = 100 / hoursCount;
	const minutes = hour / 60;
	return hour * time.hour + minutes * time.minute;
}
