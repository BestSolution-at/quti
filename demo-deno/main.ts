import { None } from './_type-utils.ts';
import { api } from './index.ts';
import { createCalendarService } from './services/CalendarServiceFetchImpl.ts';
import { createEventService } from './services/EventServiceFetchImpl.ts';

async function runDemo() {
	const calendarService = createCalendarService({
		baseUrl: 'http://127.0.0.1:8083',
	});
	const eventService = createEventService({
		baseUrl: 'http://127.0.0.1:8083',
	});

	const [calendarId, errorCreate] = await calendarService.create({
		name: 'Deno Sample',
		owner: 'Denoland',
	});

	if (calendarId === None) {
		console.error('Failed to created calender. Error:', errorCreate.message);
		return;
	}
	console.log(`Created calendar - ID: ${calendarId}`);

	const [calendar, getError] = await calendarService.get(calendarId);
	if (calendar === None) {
		console.error('Failed to load calendar. Error:', getError.message);
		return;
	}

	console.log(`Loaded calendar - Name: ${calendar.name}`);

	const [, updateError] = await calendarService.update(calendarId, {
		name: 'Sample Update calendar',
	});

	if (updateError !== None) {
		console.log('Update failed. Error:', updateError.message);
		return;
	}

	const [calendar2, getError2] = await calendarService.get(calendarId);
	if (calendar2 === None) {
		console.error('Failed to load calendar. Error:', getError2.message);
		return;
	}
	console.log(`Loaded calendar - Name: ${calendar2.name}`);

	const newEvent: api.model.EventNew = {
		title: 'Sample Event',
		description: 'This is a sample event',
		start: '2024-01-01T10:00:00+01:00[Europe/Vienna]',
		end: '2024-01-01T12:00:00+01:00[Europe/Vienna]',
		tags: [],
		referencedCalendars: [],
		repeat: {
			'@type': 'daily',
			endDate: '2024-05-01',
			interval: 1,
			timeZone: 'Europe/Vienna',
		},
	};
	const [eventId, eventCreateError] = await eventService.create(
		calendarId,
		newEvent
	);

	if (eventId === None) {
		console.error('Failed to create event', eventCreateError.message);
		return;
	}
	console.log(`Created event - ID: ${eventId}`);

	const [cancel] = await eventService.cancel(
		calendarId,
		eventId + '_2024-01-02'
	);
	if (cancel === None) {
		console.error('Failed to cancel.');
		return;
	}

	const [move] = await eventService.move(
		calendarId,
		eventId + '_2024-01-03',
		'2024-01-03T15:00:00+01:00[Europe/Vienna]',
		'2024-01-03T17:00:00+01:00[Europe/Vienna]'
	);
	if (move === None) {
		console.error('Failed move');
		return;
	}
	const [description] = await eventService.description(
		calendarId,
		eventId + '_2024-01-04',
		'Something special'
	);

	if (description === None) {
		console.error('Failed description change');
		return;
	}

	const [evUpdate, evUpdateError] = await eventService.update(
		calendarId,
		eventId,
		{
			title: 'Sample Event Updated',
		}
	);

	if (evUpdate === None) {
		console.error('Failed update. Error: ', evUpdateError.message);
	}

	{
		const [events, eventsError] = await calendarService.eventView(
			calendarId,
			'2024-01-01',
			'2024-01-05',
			'Europe/Vienna'
		);
		if (events === None) {
			console.error('Failed to fetch events: Error:', eventsError.message);
			return;
		}
		events.forEach((e) => {
			console.log(
				`${e.title}: ${e.start} - ${e.end} - ${e.status} - ${e.description}`
			);
		});
	}
	const [uncancel] = await eventService.uncancel(
		calendarId,
		eventId + '_2024-01-02'
	);
	if (uncancel === None) {
		console.error('Failed to uncancel');
		return;
	}

	console.log('=====================');

	{
		const [events, eventsError] = await calendarService.eventView(
			calendarId,
			'2024-01-01',
			'2024-01-05',
			'Europe/Vienna'
		);
		if (events === None) {
			console.error('Failed to fetch events: Error:', eventsError.message);
			return;
		}
		events.forEach((e) => {
			console.log(
				`${e.title}: ${e.start} - ${e.end} - ${e.status} - ${e.description}`
			);
		});
	}
}

// Learn more at https://docs.deno.com/runtime/manual/examples/module_metadata#concepts
if (import.meta.main) {
	//const key = 'bec0c3f5-8a84-40e2-be70-75700fed8810';

	try {
		await runDemo();
	} catch (e) {
		console.error('Technical failure', e);
	}
}
