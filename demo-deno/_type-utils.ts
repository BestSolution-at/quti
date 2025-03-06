export function isDefined<T>(value: T | undefined): value is T {
	return value !== undefined;
}

export function isUndefined<T>(value: T | undefined): value is undefined {
	return value === undefined;
}

export function isNotNull<T>(value: T | null): value is T {
	return value !== null;
}

export function isNull<T>(value: T | null): value is null {
	return value === null;
}

export function isValue<T>(value: T | undefined | null): value is T {
	return isNotNull(value) && isDefined(value);
}

export function isBoolean(value: unknown): value is boolean {
	return typeof value === 'boolean';
}

export function isNumber(value: unknown): value is number {
	return typeof value === 'number';
}

export function isArray(value: unknown): value is Array<unknown> {
	return isNotNull(value) && isDefined(value) && Array.isArray(value);
}

export function isStringType<T extends string>(
	value: unknown,
	type: T
): value is T {
	return value === type;
}

export function createIsStringTypeGuard<T extends string>(
	type: T
): (v: unknown) => v is T {
	return (v) => isStringType(v, type);
}

export function isTypedArray<T>(
	value: unknown,
	guard: (v: unknown) => v is T
): value is Array<T> {
	if (isArray(value)) {
		if (value.length === 0) {
			return true;
		}

		return value.find(guard) === undefined;
	}
	return false;
}

export function createTypedArrayGuard<T>(
	guard: (v: unknown) => v is T
): (v: unknown) => v is T[] {
	return (v) => isTypedArray(v, guard);
}

export function isRecord(value: unknown): value is Record<string, unknown> {
	return isNotNull(value) && typeof value === 'object' && !isArray(value);
}

export function isString(value: unknown): value is string {
	return typeof value === 'string';
}

export class PropertyCheckError extends Error {
	readonly property: string;
	readonly record: Record<string, unknown>;

	constructor(
		message: string,
		property: string,
		record: Record<string, unknown>
	) {
		super(message);
		this.property = property;
		this.record = record;
	}
}

export function propValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T
): T;
export function propValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	allow: 'optional'
): T | undefined;
export function propValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	allow: 'null'
): T | null;
export function propValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	allow: 'optional_null'
): T | null | undefined;
export function propValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	allow?: 'optional' | 'null' | 'optional_null'
): T | null | undefined {
	const v = record[name];
	if (allow === 'optional' || allow === 'optional_null') {
		if (isUndefined(v)) {
			return undefined;
		}
	}
	if (allow === 'null' || allow === 'optional_null') {
		if (isNull(v)) {
			return null;
		}
	}
	if (guard(v)) {
		return v;
	}

	throw new PropertyCheckError(
		`Value in property ${name} is invalid`,
		name,
		record
	);
}

export function propListValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T
): T[];
export function propListValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	allow: 'optional'
): T[] | undefined;
export function propListValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	allow: 'null'
): T[] | null;
export function propListValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	allow: 'optional_null'
): T[] | undefined | null;
export function propListValue<T>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	allow?: 'optional' | 'null' | 'optional_null'
): T[] | undefined | null {
	const v = record[name];
	if (allow === 'optional' || allow == 'optional_null') {
		if (isUndefined(v)) {
			return undefined;
		}
	}
	if (allow === 'null' || allow === 'optional_null') {
		if (isNull(v)) {
			return null;
		}
	}
	if (isTypedArray(v, guard)) {
		return v;
	}
	throw new PropertyCheckError(
		`Value in property ${name} is invalid`,
		name,
		record
	);
}

export function propMappedValue<T, U>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	map: (v: T) => U
): U;
export function propMappedValue<T, U>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	map: (v: T) => U,
	allow: 'optional'
): U | undefined;
export function propMappedValue<T, U>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	map: (v: T) => U,
	allow: 'null'
): U | null;
export function propMappedValue<T, U>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	map: (v: T) => U,
	allow: 'optional_null'
): U | undefined | null;
export function propMappedValue<T, U>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	map: (v: T) => U,
	allow?: 'optional' | 'null' | 'optional_null'
): U | undefined | null {
	const v = record[name];
	if (allow === 'optional' || allow === 'optional_null') {
		if (isUndefined(v)) {
			return undefined;
		}
	}
	if (allow === 'null' || allow === 'optional_null') {
		if (isNull(v)) {
			return null;
		}
	}
	if (guard(v)) {
		return map(v);
	}

	throw new PropertyCheckError(
		`Value in property ${name} is invalid`,
		name,
		record
	);
}

export function propMappedListValue<T, U>(
	name: string,
	record: Record<string, unknown>,
	guard: (v: unknown) => v is T,
	map: (v: T) => U,
	allow?: 'optional' | 'null' | 'optional_null'
): U[] | undefined | null {
	const v = record[name];
	if (allow === 'optional' || allow === 'optional_null') {
		if (isUndefined(v)) {
			return undefined;
		}
	}
	if (allow === 'null' || allow === 'optional_null') {
		if (isNull(v)) {
			return null;
		}
	}
	if (isTypedArray(v, guard)) {
		return v.map(map);
	}

	throw new PropertyCheckError(
		`Value in property ${name} is invalid`,
		name,
		record
	);
}

export function checkProp<T, K extends string>(
	value: Record<string, unknown>,
	property: K,
	typeCheck: (value: unknown) => value is T,
	valueCheck?: (value: T) => boolean
): value is Record<string, unknown> {
	if (property in value) {
		const v = value[property];
		return (
			v !== undefined &&
			v !== null &&
			typeCheck(v) &&
			(valueCheck === undefined || valueCheck(v))
		);
	}

	return false;
}

export function checkOptProp<T, K extends string>(
	value: Record<string, unknown>,
	property: K,
	typeCheck: (value: unknown) => value is T,
	valueCheck?: (value: T) => boolean
): value is Record<string, unknown> {
	if (!(property in value)) {
		return true;
	}
	return checkProp(value, property, typeCheck, valueCheck);
}

export type RSDError<T> = {
	_type: T;
};

export type Fetch = typeof fetch;
export type ServiceProps<T> = {
	baseUrl: string;
	fetchAPI?: Fetch;
	typeCheck?: boolean;
	lifecycleHandlers?: {
		preFetch?: (method: string) => RequestInit;
		onSuccess?: (method: string, value: unknown) => void;
		onError?: (method: string, err: RSDError<T>) => void;
		onCatch?: (method: string, err: unknown) => void;
		final?: (method: string) => void;
	};
};

export const None: unique symbol = Symbol('None');
export type NoneType = typeof None;

export function isOk<T, E>(
	value: Result<T, E>
): value is {
	readonly Ok: T;
	readonly Err: NoneType;
} {
	return value.Ok !== None;
}

export type Result<T, E> =
	| {
			readonly Ok: T;
			readonly Err: NoneType;
	  }
	| { readonly Ok: NoneType; readonly Err: E };

export function OK<T, E>(value: T): Result<T, E> {
	return {
		Ok: value,
		Err: None,
	};
}

export function ERR<T, E>(err: E): Result<T, E> {
	return {
		Ok: None,
		Err: err,
	};
}

type IfThen<T, E> = {
	then: (block: (v: T) => void) => { else: (block: (v: E) => void) => void };
	map: <U>(block: (v: T) => U) => IfThen<U, E>;
};

export function If<T, E>(result: Result<T, E>): IfThen<T, E> {
	return {
		map: <U>(block: (v: T) => U) => {
			const { Ok, Err } = result;
			if (Ok !== None) {
				return If(OK(block(Ok)));
			}
			return If(ERR(Err)) as IfThen<U, E>;
		},
		then: (block: (v: T) => void) => {
			const { Ok, Err } = result;
			if (Ok !== None) {
				block(Ok);
				return {
					else: () => {
						// Nothing
					},
				};
			}

			return {
				else: (block: (v: E) => void) => {
					if (Err !== None) {
						return block(Err);
					}
					console.error('Should not get here');
				},
			};
		},
	};
}
