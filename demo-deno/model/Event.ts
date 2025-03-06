// Generated by RSD - Do not modify
import { EventRepeat, EventRepeatFromJSON, EventRepeatToJSON, isEventRepeat } from './EventRepeat.ts';
import { checkOptProp, checkProp, createTypedArrayGuard, isBoolean, isNull, isRecord, isString, isUndefined, propListValue, propMappedValue, propValue } from '../_type-utils.ts';

export type Event = {
	readonly key: string;
	readonly title: string;
	readonly description?: string;
	readonly start: string;
	readonly end: string;
	readonly fullday?: boolean;
	readonly repeat?: EventRepeat;
	readonly tags: string[];
	readonly referencedCalendars: string[];
};

export function isEvent(value: unknown): value is Event {
	return isRecord(value) &&
		checkProp(value, 'key', isString) &&
		checkProp(value, 'title', isString) &&
		checkOptProp(value, 'description', isString) &&
		checkProp(value, 'start', isString) &&
		checkProp(value, 'end', isString) &&
		checkOptProp(value, 'fullday', isBoolean) &&
		checkOptProp(value, 'repeat', isEventRepeat) &&
		checkProp(value, 'tags', createTypedArrayGuard(isString)) &&
		checkProp(value, 'referencedCalendars', createTypedArrayGuard(isString));
}

export function EventFromJSON(value: Record<string, unknown>): Event {
	const key = propValue('key', value, isString);
	const title = propValue('title', value, isString);
	const description = propValue('description', value, isString, 'optional');
	const start = propValue('start', value, isString);
	const end = propValue('end', value, isString);
	const fullday = propValue('fullday', value, isBoolean, 'optional');
	const repeat = propMappedValue('repeat', value, isRecord, EventRepeatFromJSON, 'optional');
	const tags = propListValue('tags', value, isString);
	const referencedCalendars = propListValue('referencedCalendars', value, isString);
	return {
		key,
		title,
		description,
		start,
		end,
		fullday,
		repeat,
		tags,
		referencedCalendars,
	};
}

export function EventToJSON(value: Event): Record<string, unknown> {
	const key = value.key;
	const title = value.title;
	const description = value.description;
	const start = value.start;
	const end = value.end;
	const fullday = value.fullday;
	const repeat = isUndefined(value.repeat) ? undefined : EventRepeatToJSON(value.repeat);
	const tags = value.tags;
	const referencedCalendars = value.referencedCalendars;

	return {
		key,
		title,
		description,
		start,
		end,
		fullday,
		repeat,
		tags,
		referencedCalendars,
	};
}


export type EventPatch = {
	readonly title?: string;
	readonly description?: (string | null);
	readonly start?: string;
	readonly end?: string;
	readonly fullday?: (boolean | null);
	readonly repeat?: (EventRepeat | null);
	readonly tags?: string[];
	readonly referencedCalendars?: string[];
};

export function isEventPatch(value: unknown): value is EventPatch {
	return isRecord(value) &&
		checkOptProp(value, 'title', isString) &&
		(isNull(value.description) || checkOptProp(value, 'description', isString)) &&
		checkOptProp(value, 'start', isString) &&
		checkOptProp(value, 'end', isString) &&
		(isNull(value.fullday) || checkOptProp(value, 'fullday', isBoolean)) &&
		(isNull(value.repeat) || checkOptProp(value, 'repeat', isEventRepeat)) &&
		checkOptProp(value, 'tags', createTypedArrayGuard(isString)) &&
		checkOptProp(value, 'referencedCalendars', createTypedArrayGuard(isString));
}

export function EventPatchFromJSON(value: Record<string, unknown>): EventPatch {
	const title = propValue('title', value, isString, 'optional');
	const description = propValue('description', value, isString, 'optional_null');
	const start = propValue('start', value, isString, 'optional');
	const end = propValue('end', value, isString, 'optional');
	const fullday = propValue('fullday', value, isBoolean, 'optional_null');
	const repeat = propMappedValue('repeat', value, isRecord, EventRepeatFromJSON, 'optional_null');
	const tags = propListValue('tags', value, isString, 'optional');
	const referencedCalendars = propListValue('referencedCalendars', value, isString, 'optional');
	return {
		title,
		description,
		start,
		end,
		fullday,
		repeat,
		tags,
		referencedCalendars,
	};
}

export function EventPatchToJSON(value: EventPatch): Record<string, unknown> {
	const title = value.title;
	const description = value.description;
	const start = value.start;
	const end = value.end;
	const fullday = value.fullday;
	const repeat = isUndefined(value.repeat) || isNull(value.repeat) ? value.repeat : EventRepeatToJSON(value.repeat);
	const tags = value.tags;
	const referencedCalendars = value.referencedCalendars;

	return {
		title,
		description,
		start,
		end,
		fullday,
		repeat,
		tags,
		referencedCalendars,
	};
}

