import {
	checkProp,
	ERR,
	isRecord,
	isString,
	None,
	OK,
	Result,
	RSDError,
	ServiceProps,
} from '../_type-utils.ts';
import { Calendar, isCalendar } from '../model/Calendar.ts';

export interface CalendarService {
	get(key: string): Promise<Calendar>;
	get_(
		key: string
	): Promise<Result<Calendar, InvalidArgumentError | NotFoundError>>;
}

type SimpleErrorType = 'NotFound' | 'InvalidArgument' | 'InvalidContent';
type ErrorType = SimpleErrorType;

export type InvalidArgumentError = RSDError<'NotFound'> & { message: string };
export type NotFoundError = RSDError<'InvalidArgument'> & { message: string };
export type InvalidContentError = RSDError<'InvalidContent'> & {
	message: string;
};

const errorTypes = new Set(['NotFound', 'InvalidArgument', 'InvalidContent']);

export function isRSDError(value: unknown) {
	return (
		isRecord(value) &&
		checkProp(value, '_type', isString, errorTypes.has.bind(errorTypes))
	);
}

function createSimpleError<T extends SimpleErrorType>(
	_type: T,
	message: string
) {
	return {
		_type,
		message,
	};
}

function handle<T>(value: T, block: () => void): T {
	block();
	return value;
}

export function createCalendarService(
	props: ServiceProps<ErrorType>
): CalendarService {
	const { fetchAPI = fetch, typeCheck = true } = props;

	const get_: CalendarService['get_'] = async (key: string) => {
		try {
			const result = await fetchAPI(
				`${props.baseUrl}/api/calendar/${key}`,
				props.lifecycleHandlers?.preFetch?.('get')
			);
			if (result.status === 200) {
				const data = await result.json();
				if (typeCheck === false || isCalendar(data)) {
					return handle(OK(data), () =>
						props.lifecycleHandlers?.onSuccess?.('get', data)
					);
				} else {
					throw new Error(`Received invalid data ${data}`);
				}
			} else if (result.status === 404) {
				const err = createSimpleError('NotFound', await result.text());
				return handle(ERR(err), () =>
					props.lifecycleHandlers?.onError?.('get', err)
				);
			} else if (result.status === 400) {
				const err = createSimpleError('InvalidArgument', await result.text());
				return handle(ERR(err), () =>
					props.lifecycleHandlers?.onError?.('get', err)
				);
			}
			throw new Error(
				`Unsupported return status: ${result.status}. The status text was: ${result.statusText}`
			);
		} catch (e) {
			props.lifecycleHandlers?.onCatch?.('get', e);
			throw e;
		} finally {
			props.lifecycleHandlers?.final?.('get');
		}
	};

	return {
		get: (key: string) => {
			return get_(key).then(({ Ok, Err }) => {
				if (Ok !== None) {
					return Ok;
				}
				throw Err;
			});
		},
		get_,
	};
}
