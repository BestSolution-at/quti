// Generated by RSD - Do not modify
import { ifDefined, ServiceProps } from './_fetch-type-utils.ts';
import { api } from '../index.ts';
import { ERR, OK } from '../_type-utils';

function handle<T>(value: T, block: () => void): T {
	block();
	return value;
}
export function createEventService(props: ServiceProps<api.service.ErrorType>): api.service.EventService {
	return {
		create: fnCreate(props),
		get: fnGet(props),
		search: fnSearch(props),
		update: fnUpdate(props),
		delete: fnDelete(props),
		cancel: fnCancel(props),
		uncancel: fnUncancel(props),
		move: fnMove(props),
		endRepeat: fnEndRepeat(props),
		description: fnDescription(props),
	};
}
function fnCreate(props: ServiceProps<api.service.ErrorType>): api.service.EventService['create'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onError, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, event: api.model.EventNew) => {
		try {
			const $init = (await preFetch?.('create')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/`;
			const $body = JSON.stringify(api.model.EventNewToJSON(event));
			const $response = await fetchAPI($path, { ...$init, method: 'POST', body: $body });
			if($response.status === 201) {
				const $data = await $response.json();
				return handle(OK($data), () => onSuccess?.('create', $data));
			} else if($response.status === 404) {
				const err = {
					_type: 'NotFound',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('create', err));
			} else if($response.status === 400) {
				const err = {
					_type: 'InvalidArgument',
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

function fnGet(props: ServiceProps<api.service.ErrorType>): api.service.EventService['get'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onError, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, key: string, timezone: string) => {
		try {
			const $init = (await preFetch?.('get')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$headers.append('timezone',`${timezone}`);
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/${key}`;
			const $response = await fetchAPI($path, { ...$init, method: 'GET' });

			if($response.status === 200) {
				const $data = await $response.json();
				const $result = api.model.EventToJSON($data);
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

function fnSearch(props: ServiceProps<api.service.ErrorType>): api.service.EventService['search'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onError, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, filter: api.model.EventSearch, timezone?: string) => {
		try {
			const $init = (await preFetch?.('search')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			ifDefined(timezone, v => $headers.append('timezone',`${v}`));
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/search`;
			const $body = JSON.stringify(api.model.EventSearchToJSON(filter));
			const $response = await fetchAPI($path, { ...$init, method: 'PUT', body: $body });
			if($response.status === 200) {
				const $data = await $response.json();
				const $result = api.model.EventToJSON($data);
				return handle(OK($result), () => onSuccess?.('search', $result));
			} else if($response.status === 400) {
				const err = {
					_type: 'InvalidArgument',
					message: await $response.text(),
				} as const;
				return handle(ERR(err), () => onError?.('search', err));
			}
		} catch(e) {
			onCatch?.('search', e)
			throw e;
		} finally {
			final?.('search');
		}
		throw 'e';
	};
}

function fnUpdate(props: ServiceProps<api.service.ErrorType>): api.service.EventService['update'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onError, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, key: string, changes: api.model.Event) => {
		try {
			const $init = (await preFetch?.('update')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/${key}`;
			const $body = JSON.stringify(api.model.EventToJSON(changes));
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

function fnDelete(props: ServiceProps<api.service.ErrorType>): api.service.EventService['delete'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, key: string) => {
		try {
			const $init = (await preFetch?.('delete')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/${key}`;
			const $response = await fetchAPI($path, { ...$init, method: 'DELETE' });

			if ($response.status == 204) {
				return handle(OK(null), () => onSuccess?.('delete', null));
			}
		} catch(e) {
			onCatch?.('delete', e)
			throw e;
		} finally {
			final?.('delete');
		}
		throw 'e';
	};
}

function fnCancel(props: ServiceProps<api.service.ErrorType>): api.service.EventService['cancel'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, key: string) => {
		try {
			const $init = (await preFetch?.('cancel')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/${key}/action/cancel`;
			const $response = await fetchAPI($path, { ...$init, method: 'PUT' });

			if ($response.status == 204) {
				return handle(OK(null), () => onSuccess?.('cancel', null));
			}
		} catch(e) {
			onCatch?.('cancel', e)
			throw e;
		} finally {
			final?.('cancel');
		}
		throw 'e';
	};
}

function fnUncancel(props: ServiceProps<api.service.ErrorType>): api.service.EventService['uncancel'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, key: string) => {
		try {
			const $init = (await preFetch?.('uncancel')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/${key}/action/uncancel`;
			const $response = await fetchAPI($path, { ...$init, method: 'PUT' });

			if ($response.status == 204) {
				return handle(OK(null), () => onSuccess?.('uncancel', null));
			}
		} catch(e) {
			onCatch?.('uncancel', e)
			throw e;
		} finally {
			final?.('uncancel');
		}
		throw 'e';
	};
}

function fnMove(props: ServiceProps<api.service.ErrorType>): api.service.EventService['move'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, key: string, start: string, end: string) => {
		try {
			const $init = (await preFetch?.('move')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/${key}/action/move`;
			const $body = JSON.stringify({
				start: `${start}`,
				end: `${end}`,
			});
			const $response = await fetchAPI($path, { ...$init, method: 'PUT', body: $body });
			if ($response.status == 204) {
				return handle(OK(null), () => onSuccess?.('move', null));
			}
		} catch(e) {
			onCatch?.('move', e)
			throw e;
		} finally {
			final?.('move');
		}
		throw 'e';
	};
}

function fnEndRepeat(props: ServiceProps<api.service.ErrorType>): api.service.EventService['endRepeat'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, key: string, end: string) => {
		try {
			const $init = (await preFetch?.('endRepeat')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/${key}/action/end-repeat`;
			const $body = `${end}`;
			const $response = await fetchAPI($path, { ...$init, method: 'PUT', body: $body });
			if ($response.status == 204) {
				return handle(OK(null), () => onSuccess?.('endRepeat', null));
			}
		} catch(e) {
			onCatch?.('endRepeat', e)
			throw e;
		} finally {
			final?.('endRepeat');
		}
		throw 'e';
	};
}

function fnDescription(props: ServiceProps<api.service.ErrorType>): api.service.EventService['description'] {
	const { baseUrl, fetchAPI = fetch, lifecycleHandlers = {} } = props;
	const { preFetch, onSuccess, onCatch, final } = lifecycleHandlers;
	return async (calendar: string, key: string, description: string) => {
		try {
			const $init = (await preFetch?.('description')) ?? {};
			const $headers = new Headers($init.headers ?? {});
			$headers.append('Content-Type', 'application/json');
			$init.headers = $headers;

			const $path = `${baseUrl}/api/calendar/${calendar}/events/${key}/action/description`;
			const $body = `${description}`;
			const $response = await fetchAPI($path, { ...$init, method: 'PUT', body: $body });
			if ($response.status == 204) {
				return handle(OK(null), () => onSuccess?.('description', null));
			}
		} catch(e) {
			onCatch?.('description', e)
			throw e;
		} finally {
			final?.('description');
		}
		throw 'e';
	};
}

