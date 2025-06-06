// Generated by RSD - Do not modify
import {
	type EventRepeatDaily,
	EventRepeatDailyFromJSON,
	EventRepeatDailyToJSON,
	isEventRepeatDaily,
} from './EventRepeatDaily.js';
import {
	type EventRepeatWeekly,
	EventRepeatWeeklyFromJSON,
	EventRepeatWeeklyToJSON,
	isEventRepeatWeekly,
} from './EventRepeatWeekly.js';
import {
	type EventRepeatAbsoluteMonthly,
	EventRepeatAbsoluteMonthlyFromJSON,
	EventRepeatAbsoluteMonthlyToJSON,
	isEventRepeatAbsoluteMonthly,
} from './EventRepeatAbsoluteMonthly.js';
import {
	type EventRepeatAbsoluteYearly,
	EventRepeatAbsoluteYearlyFromJSON,
	EventRepeatAbsoluteYearlyToJSON,
	isEventRepeatAbsoluteYearly,
} from './EventRepeatAbsoluteYearly.js';
import {
	type EventRepeatRelativeMonthly,
	EventRepeatRelativeMonthlyFromJSON,
	EventRepeatRelativeMonthlyToJSON,
	isEventRepeatRelativeMonthly,
} from './EventRepeatRelativeMonthly.js';
import {
	type EventRepeatRelativeYearly,
	EventRepeatRelativeYearlyFromJSON,
	EventRepeatRelativeYearlyToJSON,
	isEventRepeatRelativeYearly,
} from './EventRepeatRelativeYearly.js';
import { isString } from '../_type-utils.js';

export type EventRepeat =
	| EventRepeatDaily
	| EventRepeatWeekly
	| EventRepeatAbsoluteMonthly
	| EventRepeatAbsoluteYearly
	| EventRepeatRelativeMonthly
	| EventRepeatRelativeYearly;

export function isEventRepeat(value: unknown) {
	return (
		isEventRepeatDaily(value) ||
		isEventRepeatWeekly(value) ||
		isEventRepeatAbsoluteMonthly(value) ||
		isEventRepeatAbsoluteYearly(value) ||
		isEventRepeatRelativeMonthly(value) ||
		isEventRepeatRelativeYearly(value)
	);
}

export function EventRepeatFromJSON(
	value: Record<string, unknown>,
): EventRepeat {
	const descriminator = value['@type'];

	if (!isString(descriminator)) {
		throw new Error('No valid descriminator found');
	}
	switch (descriminator) {
		case 'daily':
			return EventRepeatDailyFromJSON(value);
		case 'weekly':
			return EventRepeatWeeklyFromJSON(value);
		case 'absolute-monthly':
			return EventRepeatAbsoluteMonthlyFromJSON(value);
		case 'absolute-yearly':
			return EventRepeatAbsoluteYearlyFromJSON(value);
		case 'relative-monthly':
			return EventRepeatRelativeMonthlyFromJSON(value);
		case 'relative-yearly':
			return EventRepeatRelativeYearlyFromJSON(value);
		default:
			throw new Error(`Unknown descriminator "${descriminator}"`);
	}
}
export function EventRepeatToJSON(value: EventRepeat): Record<string, unknown> {
	const $desc = value['@type'];
	switch ($desc) {
		case 'daily':
			return EventRepeatDailyToJSON(value);
		case 'weekly':
			return EventRepeatWeeklyToJSON(value);
		case 'absolute-monthly':
			return EventRepeatAbsoluteMonthlyToJSON(value);
		case 'absolute-yearly':
			return EventRepeatAbsoluteYearlyToJSON(value);
		case 'relative-monthly':
			return EventRepeatRelativeMonthlyToJSON(value);
		case 'relative-yearly':
			return EventRepeatRelativeYearlyToJSON(value);
		default:
			throw new Error(`Unknown descriminator "${$desc}";`);
	}
}
