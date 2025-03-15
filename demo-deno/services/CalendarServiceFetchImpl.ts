// Generated by RSD - Do not modify
import { ifDefined, ServiceProps } from './_fetch-type-utils.ts';
import { api } from '../index.ts';
import { ERR, OK } from '../_type-utils';

function handle<T>(value: T, block: () => void): T {
	block();
	return value;
}
export function createCalendarService(props: ServiceProps<api.service.ErrorType>): api.service.CalendarService {
	return {
		create: fnCreate(props),
		get: fnGet(props),
		update: fnUpdate(props),
		eventView: fnEventView(props),
	};
}
function fnCreate(props: ServiceProps<api.service.ErrorType>): api.service.CalendarService['create'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onError, onCatch, final } = lifecycleHandlers;
	return async (calendar: api.model.CalendarNew) => {
		try {
			const $init = (await preFetch?.('create')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/`;
			const $body = JSON.stringify(api.model.CalendarNewToJSON(calendar));
			const $response = await fetchAPI($path, { ...$init, method: 'POST', body: $body });
			if($response.status === 201) {
				const $data = await $response.json();
				return handle(OK($data), () => onSuccess?.('create', $data));
			} else if($response.status === 422) {
				const err = {
					_type: 'InvalidContent',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('create', err));
			}
		} catch(e) {
			onCatch?.('create', e)
			throw e;
		} finally {
			final?.('create');
		}
		throw 'e';
	};
}

function fnGet(props: ServiceProps<api.service.ErrorType>): api.service.CalendarService['get'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onError, onCatch, final } = lifecycleHandlers;
	return async (key: string) => {
		try {
			const $init = (await preFetch?.('get')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${key}`;
			const $response = await fetchAPI($path, { ...$init, method: 'GET' });

			if($response.status === 200) {
				const $data = await $response.json();
				const $result = api.model.CalendarToJSON($data);
				return handle(OK($result), () => onSuccess?.('get', $result));
			} else if($response.status === 404) {
				const err = {
					_type: 'NotFound',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('get', err));
			} else if($response.status === 400) {
				const err = {
					_type: 'InvalidArgument',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('get', err));
			}
		} catch(e) {
			onCatch?.('get', e)
			throw e;
		} finally {
			final?.('get');
		}
		throw 'e';
	};
}

function fnUpdate(props: ServiceProps<api.service.ErrorType>): api.service.CalendarService['update'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onError, onCatch, final } = lifecycleHandlers;
	return async (key: string, changes: api.model.Calendar) => {
		try {
			const $init = (await preFetch?.('update')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${key}`;
			const $body = JSON.stringify(api.model.CalendarToJSON(changes));
			const $response = await fetchAPI($path, { ...$init, method: 'PATCH', body: $body });
			if($response.status === 204) {
				return handle(OK(null), () => onSuccess?.('update', null));
			} else if($response.status === 404) {
				const err = {
					_type: 'NotFound',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('update', err));
			} else if($response.status === 400) {
				const err = {
					_type: 'InvalidArgument',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('update', err));
			}
		} catch(e) {
			onCatch?.('update', e)
			throw e;
		} finally {
			final?.('update');
		}
		throw 'e';
	};
}

function fnEventView(props: ServiceProps<api.service.ErrorType>): api.service.CalendarService['eventView'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onError, onCatch, final } = lifecycleHandlers;
	return async (key: string, start: string, end: string, timezone: string, resultTimeZone?: string) => {
		try {
			const $init = (await preFetch?.('eventView')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			ifDefined(resultTimeZone, v => $headers.append('resultTimeZone',`${v}`));
			$init.headers = $headers;

			const $param = new URLSearchParams();
			$param.append('from', `${start}`);
			$param.append('to', `${end}`);
			$param.append('timezone', `${timezone}`);
			const $path = `${baseUrl}/api/calendar/${key}/view?${$param.toString()}`;
			const $response = await fetchAPI($path, { ...$init, method: 'GET' });

			if($response.status === 200) {
				const $data = await $response.json();
				const $result = api.model.EventViewToJSON($data);
				return handle(OK($result), () => onSuccess?.('eventView', $result));
			} else if($response.status === 404) {
				const err = {
					_type: 'NotFound',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('eventView', err));
			} else if($response.status === 400) {
				const err = {
					_type: 'InvalidArgument',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('eventView', err));
			}
		} catch(e) {
			onCatch?.('eventView', e)
			throw e;
		} finally {
			final?.('eventView');
		}
		throw 'e';
	};
}

