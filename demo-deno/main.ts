import { If, None } from './_type-utils.ts';
import {
	createCalendarService,
	isRSDError,
} from './services/TCalendarService.ts';

// Learn more at https://docs.deno.com/runtime/manual/examples/module_metadata#concepts
if (import.meta.main) {
	const key = 'bec0c3f5-8a84-40e2-be70-75700fed8810';

	const service = createCalendarService({
		baseUrl: 'http://127.0.0.1:8080',
	});
	try {
		console.log('Try - Success', (await service.get(key)).key);
	} catch (e) {
		if (isRSDError(e)) {
			console.log('Try-Catch - Error', e._type);
		} else {
			console.log('Try-Catch - Native Error', e);
		}
	}

	try {
		const { Ok, Err } = await service.get_(key);
		if (Ok !== None) {
			console.log('If - Success', Ok.key);
		} else {
			console.log('If - Error', Err._type);
		}
	} catch (e) {
		console.log('If - Native error', e);
	}

	try {
		const result = await service.get_(key);
		If(result)
			.then((v) => console.log('Functional 1 - Success', v.key))
			.else((err) => console.log('Functional 1 - Error', err._type));
	} catch (e) {
		console.log('Functional 1 - Native error', e);
	}

	try {
		const result = await service.get_(key);
		If(result)
			.map((v) => ['Functional 2 -', 'Success', v.key])
			.then((v) => console.log(...v))
			.else((err) => console.log('Functional 2 -', 'Error', err._type));
	} catch (e) {
		console.log('Functional 2 -', 'Native error', e);
	}
}
