import {
	forwardRef,
	Ref,
	useImperativeHandle,
	useLayoutEffect,
	useMemo,
	useRef,
	useState,
} from 'react';
import {
	CalendarDate,
	getLocalTimeZone,
	isSameDay,
	isWeekday,
	toCalendarDate,
	today,
} from '@internationalized/date';

import { useResizeObserver } from '@react-aria/utils';

import { QutiDayEvent, QutiMultiDayViewProperties, Range } from './View.types';
import { SpectrumCalenderScrollRef } from './DayView';
import {
	computeDayLayout,
	condensedFormat,
	createDateRange,
	hoursFromRange,
	intersects,
	LaneLayout,
	LaneLayoutEntry,
	scaleToMinSize,
} from './utils';
import { FlexView } from './generic/FlexView';
import { Flex, Item, Picker, Section, View } from '@adobe/react-spectrum';
import { ExtendedText } from './generic/ExtendedText';
import { QutiAgendaView } from './AgendaView';

import './day-view.css';
import { Header } from './components/Header';
import { DayContent } from './components/DayContent';
import { HourSegment } from './components/HourSegment';
import { FullDayEntry } from './components/FullDayEntry';

export function _QutiMultiDayView(
	props: QutiMultiDayViewProperties,
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
	const [scrollbarInsets, setScrollbarInsets] = useState(0);
	const [condensedMode, setCondensedMode] = useState(false);
	const [condensedModeDate, setCondensedModeDate] = useState(
		today(getLocalTimeZone()).toString(),
	);
	const [displayAgendaPossible, setDisplayAgendaPossible] = useState(false);

	const displayAgenda = props.showAgenda && displayAgendaPossible;

	useResizeObserver({
		onResize: () => {
			if (scrollDiv.current) {
				if (scrollDiv.current.offsetWidth !== scrollDiv.current.clientWidth) {
					setScrollbarInsets(
						scrollDiv.current.offsetWidth - scrollDiv.current.clientWidth,
					);
				} else {
					setScrollbarInsets(0);
				}

				setCondensedMode(scrollDiv.current.getBoundingClientRect().width < 400);
				setDisplayAgendaPossible(
					(
						scrollDiv.current.parentElement as HTMLElement
					).getBoundingClientRect().width > 1024,
				);
			}
		},
		ref: scrollDiv,
	});

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

	const selectedCondesedModeDate = useMemo(() => {
		if (
			condensedModeDate === 'all' ||
			condensedModeDate === 'workweek' ||
			condensedModeDate === 'agenda'
		) {
			return condensedModeDate;
		}

		if (props.dates.map((d) => d.toString()).includes(condensedModeDate)) {
			return condensedModeDate;
		} else {
			const curDate = today(getLocalTimeZone());
			const t = props.dates.find((d) => isSameDay(curDate, d));
			return t ? t.toString() : props.dates[0].toString();
		}
	}, [props.dates, condensedModeDate]);

	if (
		condensedMode &&
		selectedCondesedModeDate !== condensedModeDate &&
		!props.dates.map((d) => d.toString()).includes(condensedModeDate)
	) {
		setCondensedModeDate(selectedCondesedModeDate);
	}

	const dates = useMemo(() => {
		return props.dates.filter((d) => {
			if (condensedMode) {
				if (
					selectedCondesedModeDate === 'all' ||
					selectedCondesedModeDate === 'agenda'
				) {
					return true;
				} else if (selectedCondesedModeDate === 'workweek') {
					return isWeekday(d, 'DE');
				}
				return d.toString() === selectedCondesedModeDate;
			} else {
				return true;
			}
		});
	}, [props.dates, selectedCondesedModeDate, condensedMode]);
	const startDate = dates[0];
	const endDate = dates[dates.length - 1];
	const fullDayEntries = props.fullDayEntries;

	const dayLayout = useMemo(() => {
		const weekEntries = fullDayEntries?.filter((e) => {
			const r1 = createDateRange(
				toCalendarDate(e.start),
				toCalendarDate(e.end),
			);
			const r2 = createDateRange(startDate, endDate);
			return intersects(r1, r2, true);
		});
		if (startDate && weekEntries && weekEntries.length > 0) {
			return computeDayLayout(startDate, dates.length, weekEntries);
		}
		return undefined;
	}, [startDate, endDate, dates, fullDayEntries]);

	const condensedShowAgenda =
		condensedMode && selectedCondesedModeDate === 'agenda';

	const header = (
		<FlexView
			UNSAFE_style={{ paddingRight: scrollbarInsets }}
			direction="column"
			flexGrow={1}
			overflow="hidden"
		>
			{condensedMode && (
				<FlexView
					borderStartWidth="thin"
					borderStartColor="gray-200"
					borderBottomColor="gray-200"
					borderBottomWidth="thin"
					padding="size-100"
					flexGrow={1}
					overflow="hidden"
				>
					<Picker
						aria-label="Datum"
						selectedKey={selectedCondesedModeDate}
						onSelectionChange={(k) => setCondensedModeDate(String(k))}
						flexGrow={1}
					>
						<Section title="Tage">
							{props.dates.map((d) => (
								<Item key={d.toString()}>
									{condensedFormat.format(d.toDate(getLocalTimeZone()))}
								</Item>
							))}
						</Section>
						<Section title="Ansichten">
							<Item key="agenda">Als Agenda</Item>
							<Item key="workweek">Arbeitswoche</Item>
							<Item key="all">Ganze Woche</Item>
						</Section>
					</Picker>
				</FlexView>
			)}
			{!condensedShowAgenda && (
				<>
					<FlexView flexGrow={1} overflow="hidden" flexShrink={1}>
						{dates.map((d) => (
							<Header
								key={'day-' + d}
								date={d}
								base={100 / dates.length}
								bankholiday={props.bankholiday}
								compactness={props.compactness ?? 'default'}
							/>
						))}
					</FlexView>
					{dayLayout && dayLayout.entries.length > 0 && (
						<>
							<FullDayEvents
								dates={dates}
								layout={dayLayout}
								noColoredEventText={props.noColoredEventText ?? false}
							></FullDayEvents>
							<FlexView>
								{dates.map((d) => (
									<View
										key={'day-full-' + d}
										minHeight="size-50"
										flexBasis={100 / dates.length + '%'}
										borderStartColor={'gray-200'}
										borderStartWidth={'thin'}
									></View>
								))}
							</FlexView>
						</>
					)}
				</>
			)}
		</FlexView>
	);

	return (
		<FlexView
			backgroundColor={'gray-50'}
			direction="column"
			overflow="hidden"
			flexGrow={1}
			UNSAFE_className="calendar-view"
		>
			<FlexView
				direction="column"
				borderBottomColor={'gray-300'}
				alignItems="stretch"
				borderBottomWidth="thicker"
			>
				<Flex>
					<FlexView
						minWidth="size-600"
						justifyContent="center"
						alignItems="end"
						marginBottom={
							props.compactness === 'default' ? 'size-100' : 'size-50'
						}
					>
						<ExtendedText
							value={props.weekareaText}
							colorVersion={6}
							fontWeight="bold"
							color="gray-600"
						/>
					</FlexView>
					{header}
					{!condensedMode && displayAgenda && (
						<FlexView
							minWidth="size-3000"
							alignItems="end"
							borderStartColor="gray-300"
							borderStartWidth="thin"
						>
							<ExtendedText
								value="Termine der Woche"
								fontSize="1.4em"
								marginStart="size-100"
								marginBottom="size-100"
								UNSAFE_style={{ fontWeight: 600 }}
							/>
						</FlexView>
					)}
				</Flex>
			</FlexView>
			<FlexView overflow="hidden">
				<div
					ref={scrollDiv}
					style={{
						flexGrow: 1,
						flexDirection: 'column',
						display: 'flex',
						overflowY: 'auto',
					}}
				>
					{condensedShowAgenda ? (
						<div
							style={{
								display: 'flex',
								alignItems: 'stretch',
								flexGrow: 1,
								padding: 10,
							}}
						>
							<QutiAgendaView
								dates={dates}
								entries={props.entries}
								fullDayEntries={props.fullDayEntries}
							/>
						</div>
					) : (
						<>
							<div
								style={{ display: 'flex', alignItems: 'stretch', flexGrow: 1 }}
							>
								<div style={{ display: 'flex', flexDirection: 'column' }}>
									<View minWidth="size-600" minHeight="size-125"></View>
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
												date={new CalendarDate(0, 0, 0)}
												hour={h}
												scale={props.scale}
												showText={true}
												workhours={workhours}
												hours={hours}
											/>
										))}
									</div>
								</div>
								{dates.map((d) => (
									<DayContent
										key={'content-' + d}
										date={d}
										scale={props.scale}
										entries={props.entries}
										base={100 / dates.length}
										workhours={workhours}
										hours={hours}
										actions={props.actions}
										onAction={props.onAction}
										moreInfoRenderer={props.moreInfoRenderer}
										compatness={props.compactness ?? 'default'}
										noColoredEventText={props.noColoredEventText ?? false}
									/>
								))}
							</div>
						</>
					)}
				</div>
				{!condensedMode && displayAgenda && (
					<FlexView
						minWidth="size-3000"
						overflow="hidden auto"
						borderStartColor="gray-300"
						borderStartWidth="thin"
					>
						<View padding="size-100" flexGrow={1}>
							<QutiAgendaView
								dates={dates}
								entries={props.entries}
								fullDayEntries={props.fullDayEntries}
							/>
						</View>
					</FlexView>
				)}
			</FlexView>
		</FlexView>
	);
}

export const QutiMultiDayView = forwardRef(_QutiMultiDayView);

function FullDayEvents(props: {
	dates: CalendarDate[];
	layout: LaneLayout<LaneLayoutEntry<QutiDayEvent>>;
	noColoredEventText: boolean;
}) {
	const height =
		'calc( ' +
		props.layout.maxLanes +
		' * var(--spectrum-global-dimension-size-300, var(--spectrum-alias-size-300)))';
	return (
		<FlexView position="relative">
			{props.dates.map((d) => (
				<View
					key={'day-full-' + d}
					flexBasis={100 / props.dates.length + '%'}
					borderStartColor={'gray-200'}
					borderStartWidth={'thin'}
				>
					<div style={{ minHeight: height }}></div>
				</View>
			))}
			{props.layout.entries.map((e) => (
				<FullDayEntry
					maxLanes={props.layout.maxLanes}
					startDate={props.dates[0]}
					endDate={props.dates[props.dates.length - 1]}
					key={e.data.key}
					layoutEntry={e}
					noColoredEventText={props.noColoredEventText}
				></FullDayEntry>
			))}
		</FlexView>
	);
}
