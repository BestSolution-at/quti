import { CalendarDate, CalendarDateTime } from '@internationalized/date';
import { ReactNode } from 'react';

export interface Range<T> {
	readonly min: T;
	readonly max: T;
	readonly comparator?: (a: T, b: T) => number;
}

export type SCALE =
	| 'hour'
	| 'half-hour'
	| 'quarter-hour'
	| '10-min'
	| '6-min'
	| '5-min';
export type COMPACTNESS = 'default' | 'medium' | 'compact';

export type CalendarActionItem = {
	key: string;
	label: string;
};
export interface SpectrumDayViewProperties {
	date: CalendarDate;
	scale?: SCALE;
	entries?: SpectrumTimeEvent[];
	fullDayEntries?: SpectrumDayEvent[];
	defaultScroll?: number;
	workhours?: Range<number>;
	hours?: Range<number>;
	actions?: CalendarActionItem[];
	compactness?: COMPACTNESS;
	onAction?: (key: React.Key, date: CalendarDate, time: number) => void;
	noColoredEventText?: boolean;
	moreInfoRenderer?: (eventEntry: SpectrumTimeEvent) => ReactNode | undefined;
	bankholiday?: (date: CalendarDate) => string | undefined;
}

export interface SpectrumMultiDayViewProperties {
	dates: CalendarDate[];
	scale?: SCALE;
	entries?: SpectrumTimeEvent[];
	fullDayEntries?: SpectrumDayEvent[];
	defaultScroll?: number;
	workhours?: Range<number>;
	hours?: Range<number>;
	actions?: CalendarActionItem[];
	showAgenda?: boolean;
	weekareaText?: string;
	compactness?: COMPACTNESS;
	noColoredEventText?: boolean;
	onAction?: (key: React.Key, date: CalendarDate, time: number) => void;
	moreInfoRenderer?: (eventEntry: SpectrumTimeEvent) => ReactNode | undefined;
	bankholiday?: (date: CalendarDate) => string | undefined;
}

export type EntryColor =
	| 'blue'
	| 'red'
	| 'orange'
	| 'green'
	| 'indigo'
	| 'celery'
	| 'magenta'
	| 'yellow'
	| 'fuchsia'
	| 'seafoam'
	| 'chartreuse'
	| 'purple';

export interface SpectrumTimeEvent {
	readonly key: string;
	readonly color: EntryColor;
	readonly start: CalendarDateTime;
	readonly end: CalendarDateTime;
	readonly title: string;
	readonly actions?: CalendarActionItem[];
	readonly onAction?: (key: React.Key) => void;
	readonly icon?: string;
	readonly canceled?: boolean;
	readonly moreInfo?: readonly {
		readonly label: string;
		readonly content: string;
	}[];
}

export interface SpectrumDayEvent {
	readonly key: string;
	readonly color: EntryColor;
	readonly start: CalendarDate | CalendarDateTime;
	readonly end: CalendarDate | CalendarDateTime;
	readonly title: string;
	readonly actions?: CalendarActionItem[];
	readonly onAction?: (key: React.Key) => void;
	readonly icon?: string;
	readonly canceled?: boolean;
}
