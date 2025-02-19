import { ReactNode, useMemo } from 'react';

import {
	CalendarDate,
	isSameDay,
	Time,
	toCalendarDate,
} from '@internationalized/date';

import {
	COMPACTNESS,
	QutiActionItem,
	QutiTimeEvent,
	Range,
	SCALE,
} from '../View.types';
import { computeTimeLayout, scaleToMinSize, toTimeFraction } from '../utils';
import { FlexView } from '../generic/FlexView';
import { View } from '@adobe/react-spectrum';
import { HourSegment } from './HourSegment';
import { DayEntry } from './DayEntry';

export function DayContent(props: {
	base: number;
	date: CalendarDate;
	scale?: SCALE;
	entries?: QutiTimeEvent[];
	workhours: Range<number>;
	hours: number[];
	actions?: QutiActionItem[];
	compatness: COMPACTNESS;
	noColoredEventText: boolean;
	onAction?: (key: React.Key, date: CalendarDate, time: number) => void;
	moreInfoRenderer?: (eventEntry: QutiTimeEvent) => ReactNode | undefined;
}) {
	const compute = () => {
		if (props.entries === undefined) {
			return [];
		}
		return props.entries.filter((e) => {
			return (
				isSameDay(toCalendarDate(e.start), props.date) ||
				isSameDay(toCalendarDate(e.end), props.date)
			);
		});
	};

	const entries = useMemo(compute, [props.date, props.entries]);
	const layout = useMemo(
		() => computeTimeLayout(props.date, entries, props.hours.length),
		[props.date, entries, props.hours],
	);
	const startShift =
		props.hours[0] === 0
			? 0
			: toTimeFraction(new Time(props.hours[0], 0), props.hours.length);

	const styles: React.CSSProperties = {
		minHeight: scaleToMinSize(
			props.scale ?? 'half-hour',
			props.hours.length,
			props.compatness,
		),
		position: 'relative',
		flexGrow: 1,
	};
	(styles as Record<string, unknown>)['--work-day-start'] =
		props.workhours.min - props.hours[0];
	(styles as Record<string, unknown>)['--work-day-end'] =
		props.workhours.max - props.hours[0];
	(styles as Record<string, unknown>)['--hours-count'] = props.hours.length;

	return (
		<FlexView flexBasis={props.base + '%'} position="relative">
			<FlexView
				backgroundColor={'gray-100'}
				flexGrow={1}
				direction="column"
				borderStartColor={'gray-400'}
				borderStartWidth={'thin'}
			>
				<View minHeight="size-125"></View>
				<div style={styles} className="hours-grid">
					{props.hours.map((h) => (
						<HourSegment
							key={'line-' + h}
							date={props.date}
							hour={h}
							scale={props.scale}
							showText={false}
							workhours={props.workhours}
							hours={props.hours}
							actions={props.actions}
							onAction={props.onAction}
						/>
					))}
					{layout.entries.map((e) => (
						<DayEntry
							key={e.data.key}
							layoutEntry={e}
							maxLanes={layout.maxLanes}
							startShift={startShift}
							moreInfoRenderer={props.moreInfoRenderer}
							compactness={props.compatness}
							noColoredEventText={props.noColoredEventText}
						/>
					))}
				</div>
			</FlexView>
		</FlexView>
	);
}
