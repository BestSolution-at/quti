// Generated by RSD - Do not modify
import { checkOptProp, checkProp, createIsStringTypeGuard, isNull, isNumber, isRecord, isString, propValue } from '../_type-utils.ts';

export type EventRepeatDaily = {
	'@type': 'daily',
	readonly interval: number;
	readonly endDate?: string;
	readonly timeZone: string;
};

export function isEventRepeatDaily(value: unknown): value is EventRepeatDaily {
	return isRecord(value) &&
		checkProp(value, '@type', createIsStringTypeGuard('daily')) &&
		checkProp(value, 'interval', isNumber) &&
		checkOptProp(value, 'endDate', isString) &&
		checkProp(value, 'timeZone', isString);
}

export function EventRepeatDailyFromJSON(value: Record<string, unknown>): EventRepeatDaily {
	const interval = propValue('interval', value, isNumber);
	const endDate = propValue('endDate', value, isString, 'optional');
	const timeZone = propValue('timeZone', value, isString);
	return {
		'@type': 'daily',
		interval,
		endDate,
		timeZone,
	};
}

export function EventRepeatDailyToJSON(value: EventRepeatDaily): Record<string, unknown> {
	const interval = value.interval;
	const endDate = value.endDate;
	const timeZone = value.timeZone;

	return {
		'@type': 'daily',
		interval,
		endDate,
		timeZone,
	};
}


export type EventRepeatDailyPatch = {
	'@type': 'daily-patch',
	readonly interval?: number;
	readonly endDate?: (string | null);
	readonly timeZone?: string;
};

export function isEventRepeatDailyPatch(value: unknown): value is EventRepeatDailyPatch {
	return isRecord(value) &&
		checkProp(value, '@type', createIsStringTypeGuard('daily')) &&
		checkOptProp(value, 'interval', isNumber) &&
		(isNull(value.endDate) || checkOptProp(value, 'endDate', isString)) &&
		checkOptProp(value, 'timeZone', isString);
}

export function EventRepeatDailyPatchFromJSON(value: Record<string, unknown>): EventRepeatDailyPatch {
	const interval = propValue('interval', value, isNumber, 'optional');
	const endDate = propValue('endDate', value, isString, 'optional_null');
	const timeZone = propValue('timeZone', value, isString, 'optional');
	return {
		'@type': 'daily-patch',
		interval,
		endDate,
		timeZone,
	};
}

export function EventRepeatDailyPatchToJSON(value: EventRepeatDailyPatch): Record<string, unknown> {
	const interval = value.interval;
	const endDate = value.endDate;
	const timeZone = value.timeZone;

	return {
		'@type': 'daily-patch',
		interval,
		endDate,
		timeZone,
	};
}

