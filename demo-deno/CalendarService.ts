// Generated by RSD - Do not modify
import { CalendarNew } from './model/CalendarNew.ts';
import { Result } from './_type-utils.ts';
import { InvalidArgumentError, InvalidContentError, NotFoundError } from './Errors.ts';
import { Calendar } from './model/Calendar.ts';
import { EventView } from './model/EventView.ts';

export interface CalendarService {
	create(calendar: CalendarNew): Promise<Result<string, InvalidContentError>>;
	get(key: string): Promise<Result<Calendar, NotFoundError | InvalidArgumentError>>;
	update(key: string, changes: Calendar): Promise<Result<void, NotFoundError | InvalidArgumentError>>;
	eventView(key: string, start: string, end: string, timezone: string, resultTimeZone?: string): Promise<Result<EventView[], NotFoundError | InvalidArgumentError>>;
}
