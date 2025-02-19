import {
	Ref,
	forwardRef,
	useImperativeHandle,
	useLayoutEffect,
	useMemo,
	useRef,
} from 'react';

import { View } from '@adobe/react-spectrum';

import {
	Range,
	QutiDayEvent,
	QutiDayViewProperties,
	QutiTimeEvent,
} from './View.types';

import { hoursFromRange, scaleToMinSize } from './utils';

import './day-view.css';

import { FlexView } from './generic/FlexView';
import { Header } from './components/Header';
import { DayContent } from './components/DayContent';
import { HourSegment } from './components/HourSegment';

export interface SpectrumCalenderScrollRef {
	scrollToEvent(event: QutiTimeEvent): void;
	scrollToDayStart(): void;
}

export function _SpectrumDayView(
	props: QutiDayViewProperties,
	ref: Ref<SpectrumCalenderScrollRef>,
) {
	const workhours: Range<number> = useMemo(
		() => (props.workhours ? props.workhours : { min: 8, max: 17 }),
		[props.workhours],
	);
	const hours: number[] = useMemo(
		() => hoursFromRange(props.hours),
		[props.hours],
	);
	const scrollDiv = useRef<HTMLDivElement>(null);

	const scrollRef = {
		scrollToEvent: (evt: QutiDayEvent) => {
			const target = scrollDiv.current?.querySelector(
				`[id='event-${evt.key}']`,
			);
			target?.scrollIntoView({ behavior: 'smooth' });
		},
		scrollToDayStart: () => {
			const target = scrollDiv.current?.querySelector(
				`[id='full-time-slot-${props.workhours?.min || 8}']`,
			);
			target?.scrollIntoView({ behavior: 'smooth' });
		},
	};

	useImperativeHandle(ref, () => scrollRef);

	useLayoutEffect(() => {
		scrollRef.scrollToDayStart();
	}, []);

	return (
		<FlexView
			backgroundColor={'gray-50'}
			overflow="hidden"
			direction="column"
			flexGrow={1}
			UNSAFE_className="calendar-view"
		>
			<FlexView borderBottomColor={'gray-300'} borderBottomWidth="thicker">
				<View minWidth="size-600"></View>
				<FlexView direction="column" flexGrow={1}>
					<Header
						date={props.date}
						base={100}
						bankholiday={props.bankholiday}
						compactness={props.compactness ?? 'default'}
					/>
					{props.fullDayEntries?.map((e) => (
						<SingleDayFull key={e.key} event={e} />
					))}
				</FlexView>
			</FlexView>
			<div
				ref={scrollDiv}
				style={{
					flexGrow: 1,
					flexDirection: 'column',
					display: 'flex',
					overflowY: 'auto',
				}}
			>
				<div style={{ display: 'flex', flexGrow: 1 }}>
					<div
						style={{ minWidth: 48, display: 'flex', flexDirection: 'column' }}
					>
						<div style={{ width: 48, minHeight: 10 }}></div>
						<div
							style={{
								minHeight: scaleToMinSize(
									props.scale ?? 'half-hour',
									hours.length,
									props.compactness ?? 'default',
								),
								position: 'relative',
								flexGrow: 1,
							}}
						>
							{hours.map((h) => (
								<HourSegment
									key={'hour-line-' + h}
									date={props.date}
									hour={h}
									scale={props.scale}
									showText={true}
									workhours={workhours}
									hours={hours}
								/>
							))}
						</div>
					</div>
					<DayContent
						base={100}
						date={props.date}
						scale={props.scale}
						entries={props.entries}
						workhours={workhours}
						hours={hours}
						actions={props.actions}
						onAction={props.onAction}
						moreInfoRenderer={props.moreInfoRenderer}
						compatness={props.compactness ?? 'default'}
						noColoredEventText={props.noColoredEventText ?? false}
					/>
				</div>
			</div>
		</FlexView>
	);
}

function SingleDayFull(props: { event: QutiDayEvent }) {
	const className = 'day-event type-' + props.event.color;
	return (
		<FlexView
			borderStartColor={'gray-200'}
			borderStartWidth={'thin'}
			paddingStart={4}
			paddingEnd={4}
			paddingBottom={4}
		>
			<div style={{ position: 'relative', flexGrow: 1 }}>
				<div className={className}>
					<span style={{ fontWeight: 600 }}>{props.event.title}</span>
				</div>
			</div>
		</FlexView>
	);
}

export const QutiDayView = forwardRef(_SpectrumDayView);
