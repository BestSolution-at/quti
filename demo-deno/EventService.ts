// Generated by RSD - Do not modify
import { EventNew } from './model/EventNew.ts';
import { Result, VoidType } from './_type-utils.ts';
import { InvalidArgumentError, NotFoundError } from './Errors.ts';
import { Event, EventPatch } from './model/Event.ts';
import { EventSearch } from './model/EventSearch.ts';

export interface EventService {
	create(calendar: string, event: EventNew): Promise<Result<string, NotFoundError | InvalidArgumentError>>;
	get(calendar: string, key: string, timezone: string): Promise<Result<Event, NotFoundError | InvalidArgumentError>>;
	search(calendar: string, filter: EventSearch, timezone?: string): Promise<Result<Event[], InvalidArgumentError>>;
	update(calendar: string, key: string, changes: EventPatch): Promise<Result<VoidType, NotFoundError | InvalidArgumentError>>;
	delete(calendar: string, key: string): Promise<Result<VoidType, never>>;
	cancel(calendar: string, key: string): Promise<Result<VoidType, never>>;
	uncancel(calendar: string, key: string): Promise<Result<VoidType, never>>;
	move(calendar: string, key: string, start: string, end: string): Promise<Result<VoidType, never>>;
	endRepeat(calendar: string, key: string, end: string): Promise<Result<VoidType, never>>;
	description(calendar: string, key: string, description: string): Promise<Result<VoidType, never>>;
}
