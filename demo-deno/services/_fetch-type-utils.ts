import { api } from '../index.ts';

export type Fetch = typeof fetch;
export type ServiceProps<T> = {
	baseUrl: string;
	fetchAPI?: Fetch;
	lifecycleHandlers?: {
		preFetch?: (method: string) => RequestInit | Promise<RequestInit>;
		onSuccess?: (method: string, value: unknown) => void;
		onError?: (method: string, err: api.utils.RSDError<T>) => void;
		onCatch?: (method: string, err: unknown) => void;
		final?: (method: string) => void;
	};
};

export function ifDefined<T>(value: T | undefined, block: (v: T) => void) {
	if (value !== undefined) {
		block(value);
	}
}

export function safeExecute<T>(value: T, block: () => void): T {
	try {
		block();
	} catch (e) {
		console.error('Failed running block', e);
	}

	return value;
}
