export const None: unique symbol = Symbol('None');
export type NoneType = typeof None;
export const Void: unique symbol = Symbol('Void');
export type VoidType = typeof Void;

export function isOk<T, E>(value: Result<T, E>): value is [T, NoneType] {
	return value[0] !== None;
}

type Ok<T> = readonly [ok: T, error: NoneType];
type Err<E> = readonly [ok: NoneType, error: E];

export type Result<T, E> = Ok<T> | Err<E>;

export function OK<T>(value: T): Ok<T> {
	return [value, None];
}

export function ERR<E>(err: E): Err<E> {
	return [None, err];
}

export type RSDError<T> = {
	_type: T;
};
