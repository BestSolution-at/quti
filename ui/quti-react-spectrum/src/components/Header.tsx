import {
	CalendarDate,
	getLocalTimeZone,
	isSameDay,
	isToday,
	startOfMonth,
} from '@internationalized/date';
import { COMPACTNESS } from '../View.types';
import { FlexView } from '../generic/FlexView';
import { ExtendedText } from '../generic/ExtendedText';
import { DAY, MONTH_AND_DAY, WEEK_DAY } from '../utils';

export function Header(props: {
	date: CalendarDate;
	base: number;
	compactness: COMPACTNESS;
	bankholiday?: (date: CalendarDate) => string | undefined;
}) {
	const today = isToday(props.date, getLocalTimeZone());
	const isFirsDayOfMonth = isSameDay(startOfMonth(props.date), props.date);
	const date = props.date.toDate(getLocalTimeZone());
	const bankholidayName = props.bankholiday
		? props.bankholiday(props.date)
		: '';

	return (
		<FlexView flexBasis={props.base + '%'} overflow="hidden">
			<FlexView
				flexGrow={1}
				borderStartColor={'gray-200'}
				borderStartWidth={'thin'}
				alignItems={'end'}
				UNSAFE_className={today ? 'header-today' : undefined}
				paddingTop={props.compactness === 'default' ? 'size-200' : undefined}
			>
				<FlexView
					alignItems={'baseline'}
					columnGap={'size-100'}
					paddingStart={'size-100'}
					marginBottom={
						props.compactness === 'default' ? 'size-100' : 'size-50'
					}
					wrap
				>
					<ExtendedText
						value={
							today || isFirsDayOfMonth
								? MONTH_AND_DAY.format(date).replace(/\./g, '')
								: DAY.format(date)
						}
						fontWeight={600}
						colorVersion={6}
						color={
							bankholidayName ? 'red-1000' : today ? 'blue-1000' : undefined
						}
						fontSize="1.4em"
					/>
					<ExtendedText
						value={WEEK_DAY.format(date)}
						colorVersion={6}
						fontWeight={400}
						color={bankholidayName ? 'red-1000' : 'gray-600'}
					></ExtendedText>
				</FlexView>
			</FlexView>
		</FlexView>
	);
}
