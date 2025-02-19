import {
	ReactNode,
	Ref,
	forwardRef,
	useImperativeHandle,
	useLayoutEffect,
	useMemo,
	useRef,
	useState,
} from 'react';

import {
	Dialog,
	DialogTrigger,
	Divider,
	Grid,
	Heading,
	Item,
	Menu,
	MenuTrigger,
	Picker,
	Section,
	Content as SpectrumContent,
	View,
} from '@adobe/react-spectrum';
import { Pressable } from '@react-aria/interactions';

import HelpOutline from '@spectrum-icons/workflow/HelpOutline';
import ChevronLeft from '@spectrum-icons/workflow/ChevronLeft';
import ChevronRight from '@spectrum-icons/workflow/ChevronRight';

import {
	CalendarDate,
	CalendarDateTime,
	DateFormatter,
	Time,
	getLocalTimeZone,
	isSameDay,
	isToday,
	isWeekday,
	parseTime,
	startOfMonth,
	toCalendarDate,
	toTime,
	today,
} from '@internationalized/date';

import { useResizeObserver } from '@react-aria/utils';

import {
	COMPACTNESS,
	CalendarActionItem,
	Range,
	SCALE,
	SpectrumDayEvent,
	SpectrumDayViewProperties,
	SpectrumMultiDayViewProperties,
	SpectrumTimeEvent,
} from './SpectrumDayView.types';

import {
	Duration,
	LaneLayout,
	LaneLayoutEntry,
	computeLaneLayout,
	createDateRange,
	intersects,
} from './laneLayoutUtil';

import './day-view.css';
import { BorderColorValue, ColorValueV6 } from '@react-types/shared';
import { SpectrumFlex } from './SpectrumFlex';
import { SpectrumText } from './SpectrumText';

const ALL_HOURS = [
	0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
	22, 23,
];

const WEEK_DAY = new DateFormatter('de', { weekday: 'short' });
const MONTH_AND_DAY = new DateFormatter('de', {
	month: 'short',
	day: '2-digit',
});
const DAY = new DateFormatter('de', { day: '2-digit' });
const TIME_FORMAT = new DateFormatter('de', {
	hour: '2-digit',
	minute: '2-digit',
});

const condensedFormat = new DateFormatter('de', {
	weekday: 'long',
	day: '2-digit',
	month: '2-digit',
});

function scaleToMinSize(
	scale: SCALE,
	hoursCount: number,
	compactness: COMPACTNESS,
) {
	let segmentHeight = 30;

	if (compactness === 'compact') {
		segmentHeight = 15;
	} else if (compactness === 'medium') {
		segmentHeight = 25;
	}

	if (scale === 'hour') {
		return segmentHeight * hoursCount;
	} else if (scale === 'quarter-hour') {
		return segmentHeight * hoursCount * 4;
	} else if (scale === '10-min') {
		return segmentHeight * hoursCount * 6;
	} else if (scale === '6-min') {
		return segmentHeight * hoursCount * 10;
	} else if (scale === '5-min') {
		return segmentHeight * hoursCount * 12;
	}
	return segmentHeight * hoursCount * 2;
}

function hoursFromRange(range?: Range<number>) {
	if (range) {
		let min = Math.max(0, Math.trunc(range.min));
		const max = Math.min(24, Math.trunc(range.max));

		if (min < max) {
			const hours: number[] = [];
			while (min < max) {
				hours.push(min);
				min += 1;
			}
			console.log(hours);
			return hours;
		}
	}
	return ALL_HOURS;
}

export interface SpectrumCalenderScrollRef {
	scrollToEvent(event: SpectrumTimeEvent): void;
	scrollToDayStart(): void;
}

export function _SpectrumDayView(
	props: SpectrumDayViewProperties,
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
		scrollToEvent: (evt: SpectrumDayEvent) => {
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
		<SpectrumFlex
			backgroundColor={'gray-50'}
			overflow="hidden"
			direction="column"
			flexGrow={1}
			UNSAFE_className="calendar-view"
		>
			<SpectrumFlex borderBottomColor={'gray-300'} borderBottomWidth="thicker">
				<View minWidth="size-600"></View>
				<SpectrumFlex direction="column" flexGrow={1}>
					<Header
						date={props.date}
						base={100}
						bankholiday={props.bankholiday}
						compactness={props.compactness ?? 'default'}
					/>
					{props.fullDayEntries?.map((e) => (
						<SingleDayFull key={e.key} event={e} />
					))}
				</SpectrumFlex>
			</SpectrumFlex>
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
					<Content
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
		</SpectrumFlex>
	);
}

function SingleDayFull(props: { event: SpectrumDayEvent }) {
	const className = 'day-event type-' + props.event.color;
	return (
		<SpectrumFlex
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
		</SpectrumFlex>
	);
}

export const SpectrumDayView = forwardRef(_SpectrumDayView);

export function _SpectrumMultiDayView(
	props: SpectrumMultiDayViewProperties,
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
		scrollToEvent: (evt: SpectrumDayEvent) => {
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
		<SpectrumFlex
			style={{ paddingRight: scrollbarInsets }}
			direction="column"
			flexGrow={1}
			overflow="hidden"
		>
			{condensedMode && (
				<SpectrumFlex
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
				</SpectrumFlex>
			)}
			{!condensedShowAgenda && (
				<>
					<SpectrumFlex flexGrow={1} overflow="hidden" flexShrink={1}>
						{dates.map((d) => (
							<Header
								key={'day-' + d}
								date={d}
								base={100 / dates.length}
								bankholiday={props.bankholiday}
								compactness={props.compactness ?? 'default'}
							/>
						))}
					</SpectrumFlex>
					{dayLayout && dayLayout.entries.length > 0 && (
						<>
							<FullDayEvents
								dates={dates}
								layout={dayLayout}
								noColoredEventText={props.noColoredEventText ?? false}
							></FullDayEvents>
							<SpectrumFlex>
								{dates.map((d) => (
									<View
										key={'day-full-' + d}
										minHeight="size-50"
										flexBasis={100 / dates.length + '%'}
										borderStartColor={'gray-200'}
										borderStartWidth={'thin'}
									></View>
								))}
							</SpectrumFlex>
						</>
					)}
				</>
			)}
		</SpectrumFlex>
	);

	return (
		<SpectrumFlex
			backgroundColor={'gray-50'}
			direction="column"
			overflow="hidden"
			flexGrow={1}
			UNSAFE_className="calendar-view"
		>
			<SpectrumFlex
				direction="column"
				borderBottomColor={'gray-300'}
				alignItems="stretch"
				borderBottomWidth="thicker"
			>
				<SpectrumFlex>
					<SpectrumFlex
						minWidth="size-600"
						justifyContent="center"
						alignItems="end"
						marginBottom={
							props.compactness === 'default' ? 'size-100' : 'size-50'
						}
					>
						<SpectrumText
							value={props.weekareaText}
							colorVersion={6}
							fontWeight="bold"
							color="gray-600"
						/>
					</SpectrumFlex>
					{header}
					{!condensedMode && displayAgenda && (
						<SpectrumFlex
							minWidth="size-3000"
							alignItems="end"
							borderStartColor="gray-300"
							borderStartWidth="thin"
						>
							<SpectrumText
								value="Termine der Woche"
								fontSize="1.4em"
								marginStart="size-100"
								marginBottom="size-100"
								UNSAFE_style={{ fontWeight: 600 }}
							/>
						</SpectrumFlex>
					)}
				</SpectrumFlex>
			</SpectrumFlex>
			<SpectrumFlex overflow="hidden">
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
							<SpectrumAgenda
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
									<Content
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
					<SpectrumFlex
						minWidth="size-3000"
						overflow="hidden auto"
						borderStartColor="gray-300"
						borderStartWidth="thin"
					>
						<View padding="size-100" flexGrow={1}>
							<SpectrumAgenda
								dates={dates}
								entries={props.entries}
								fullDayEntries={props.fullDayEntries}
							/>
						</View>
					</SpectrumFlex>
				)}
			</SpectrumFlex>
		</SpectrumFlex>
	);
}

export function SpectrumAgenda(props: {
	dates: CalendarDate[];
	entries?: SpectrumTimeEvent[];
	fullDayEntries?: SpectrumDayEvent[];
}) {
	return (
		<Grid columns="6em 1fr" width="100%" flexGrow={1}>
			{props.dates.map((d) => (
				<SpectrumAgendaDay
					key={d.toString()}
					date={d}
					entries={props.entries}
					fullDayEntries={props.fullDayEntries}
				/>
			))}
		</Grid>
	);
}

function SpectrumAgendaDay(props: {
	date: CalendarDate;
	entries?: SpectrumTimeEvent[];
	fullDayEntries?: SpectrumDayEvent[];
}) {
	const filter = (e: {
		start: CalendarDate | CalendarDateTime;
		end: CalendarDate | CalendarDateTime;
	}) => {
		const r1 = createDateRange(toCalendarDate(e.start), toCalendarDate(e.end));
		const r2 = createDateRange(props.date, props.date);
		return intersects(r1, r2, true);
	};

	const fullEntries = props.fullDayEntries?.filter(filter) ?? [];
	const timeEntries =
		props.entries?.filter(filter).sort((a, b) => a.start.compare(b.start)) ??
		[];

	return (
		<>
			<View
				marginTop="size-100"
				UNSAFE_style={{
					position: 'sticky',
					top: 0,
					boxShadow: '0px 5px 10px var(--spectrum-global-color-gray-50)',
				}}
				backgroundColor="gray-50"
			>
				<SpectrumText
					fontWeight="bold"
					value={new DateFormatter('de', {
						day: '2-digit',
						month: 'short',
					}).format(props.date.toDate(getLocalTimeZone()))}
				/>
			</View>
			<View
				marginTop="size-100"
				UNSAFE_style={{
					position: 'sticky',
					top: 0,
					boxShadow: '0px 5px 10px var(--spectrum-global-color-gray-50)',
				}}
				backgroundColor="gray-50"
				borderStartWidth="thicker"
				borderStartColor="transparent"
			>
				<SpectrumText
					color="gray-500"
					value={new DateFormatter('de', { weekday: 'long' }).format(
						props.date.toDate(getLocalTimeZone()),
					)}
				/>
			</View>
			{fullEntries.map((e) => (
				<SpectrumAgendaDayEntry key={e.key} entry={e} />
			))}
			{timeEntries.map((e) => (
				<SpectrumAgendaEntry key={e.key} entry={e}></SpectrumAgendaEntry>
			))}
			{timeEntries.length === 0 && fullEntries.length === 0 && (
				<SpectrumFlex
					gridColumnStart="1"
					gridColumnEnd="3"
					marginTop="size-100"
					backgroundColor="gray-75"
					borderRadius="small"
					paddingY="size-50"
					direction="column"
					alignItems="center"
					borderColor="gray-100"
					borderWidth="thick"
				>
					<SpectrumText
						value="Keine Termine"
						fontSize="0.9em"
						fontWeight="bold"
						colorVersion={6}
						color="gray-500"
					></SpectrumText>
				</SpectrumFlex>
			)}
		</>
	);
}

function SpectrumAgendaEntry(props: { entry: SpectrumTimeEvent }) {
	const [open, setOpen] = useState(false);

	const backgroundColor = (props.entry.color + '-100') as ColorValueV6;
	const borderColor = (props.entry.color + '-1000') as ColorValueV6;
	const textColor = (props.entry.color + '-1100') as ColorValueV6;

	const eventEntry = (
		<SpectrumFlex
			backgroundColor={backgroundColor}
			marginTop="size-100"
			colorVersion={6}
			borderRadius="small"
			gridColumn="1"
			gridColumnEnd="3"
			width="100%"
			borderStartColor={borderColor}
			borderStartWidth="thicker"
		>
			<Grid columns="6em auto" flexGrow={1}>
				<SpectrumFlex
					paddingY="size-50"
					paddingStart="size-100"
					alignItems="center"
				>
					<SpectrumText
						color={textColor}
						colorVersion={6}
						fontSize="0.7em"
						fontWeight="bold"
						value={
							new DateFormatter('de', {
								hour: '2-digit',
								minute: '2-digit',
							}).format(props.entry.start.toDate(getLocalTimeZone())) +
							' - ' +
							new DateFormatter('de', {
								hour: '2-digit',
								minute: '2-digit',
							}).format(props.entry.end.toDate(getLocalTimeZone()))
						}
					/>
				</SpectrumFlex>
				<SpectrumFlex
					paddingY="size-50"
					paddingEnd="size-100"
					alignItems="center"
				>
					<SpectrumText
						color={textColor}
						colorVersion={6}
						fontWeight="bold"
						fontSize="0.9em"
						value={props.entry.title}
					/>
				</SpectrumFlex>
			</Grid>
		</SpectrumFlex>
	);

	if (props.entry.actions?.length) {
		return (
			<MenuTrigger
				direction="end"
				trigger="longPress"
				isOpen={open}
				onOpenChange={setOpen}
			>
				<Pressable>
					<div
						style={{ gridColumn: 1, gridColumnEnd: 3 }}
						onContextMenu={(e) => {
							e.stopPropagation();
							e.preventDefault();
							setOpen(true);
						}}
					>
						{eventEntry}
					</div>
				</Pressable>
				<Menu
					onAction={(key) => props.entry.onAction && props.entry.onAction(key)}
				>
					{props.entry.actions.map((e) => (
						<Item key={e.key}>{e.label}</Item>
					))}
				</Menu>
			</MenuTrigger>
		);
	}
	return eventEntry;
}

function SpectrumAgendaDayEntry(props: { entry: SpectrumDayEvent }) {
	const backgroundColor = (props.entry.color + '-100') as ColorValueV6;
	const borderColor = (props.entry.color + '-1000') as ColorValueV6;
	const textColor = (props.entry.color + '-1100') as ColorValueV6;
	return (
		<SpectrumFlex
			backgroundColor={backgroundColor}
			marginTop="size-100"
			colorVersion={6}
			borderRadius="small"
			gridColumn="1"
			gridColumnEnd="3"
			width="100%"
			borderStartColor={borderColor}
			borderStartWidth="thicker"
		>
			<Grid columns="6em auto" flexGrow={1}>
				<SpectrumFlex
					paddingY="size-50"
					paddingStart="size-100"
					alignItems="center"
				>
					<SpectrumText
						color={textColor}
						colorVersion={6}
						fontSize="0.7em"
						fontWeight="bold"
						value="ganztägig"
					/>
				</SpectrumFlex>
				<View paddingY="size-50" paddingEnd="size-100">
					<SpectrumText
						color={textColor}
						colorVersion={6}
						fontWeight="bold"
						fontSize="0.9em"
						value={props.entry.title}
					/>
				</View>
			</Grid>
		</SpectrumFlex>
	);
}

export const SpectrumMultiDayView = forwardRef(_SpectrumMultiDayView);

function FullDayEvents(props: {
	dates: CalendarDate[];
	layout: LaneLayout<LaneLayoutEntry<SpectrumDayEvent>>;
	noColoredEventText: boolean;
}) {
	const height =
		'calc( ' +
		props.layout.maxLanes +
		' * var(--spectrum-global-dimension-size-300, var(--spectrum-alias-size-300)))';
	return (
		<SpectrumFlex position="relative">
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
		</SpectrumFlex>
	);
}

function Header(props: {
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
		<SpectrumFlex flexBasis={props.base + '%'} overflow="hidden">
			<SpectrumFlex
				flexGrow={1}
				borderStartColor={'gray-200'}
				borderStartWidth={'thin'}
				alignItems={'end'}
				UNSAFE_className={today ? 'header-today' : undefined}
				paddingTop={props.compactness === 'default' ? 'size-200' : undefined}
			>
				<SpectrumFlex
					alignItems={'baseline'}
					columnGap={'size-100'}
					paddingStart={'size-100'}
					marginBottom={
						props.compactness === 'default' ? 'size-100' : 'size-50'
					}
					wrap
				>
					<SpectrumText
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
					<SpectrumText
						value={WEEK_DAY.format(date)}
						colorVersion={6}
						fontWeight={400}
						color={bankholidayName ? 'red-1000' : 'gray-600'}
					></SpectrumText>
				</SpectrumFlex>
			</SpectrumFlex>
		</SpectrumFlex>
	);
}

function Content(props: {
	base: number;
	date: CalendarDate;
	scale?: SCALE;
	entries?: SpectrumTimeEvent[];
	workhours: Range<number>;
	hours: number[];
	actions?: CalendarActionItem[];
	compatness: COMPACTNESS;
	noColoredEventText: boolean;
	onAction?: (key: React.Key, date: CalendarDate, time: number) => void;
	moreInfoRenderer?: (eventEntry: SpectrumTimeEvent) => ReactNode | undefined;
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
		<SpectrumFlex flexBasis={props.base + '%'} position="relative">
			<SpectrumFlex
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
			</SpectrumFlex>
		</SpectrumFlex>
	);
}

function computeBottoms(
	hour: number,
	segment: number,
	scale: 'quarter-hour' | '10-min' | '6-min' | '5-min',
) {
	if (scale === 'quarter-hour') {
		const subsegment = segment / 4;
		return [
			100 - (segment * hour + subsegment),
			100 - (segment * hour + 2 * subsegment),
			100 - (segment * hour + 3 * subsegment),
		];
	} else if (scale === '10-min') {
		const subsegment = segment / 6;
		return [
			100 - (segment * hour + subsegment),
			100 - (segment * hour + 2 * subsegment),
			100 - (segment * hour + 3 * subsegment),
			100 - (segment * hour + 4 * subsegment),
			100 - (segment * hour + 5 * subsegment),
		];
	} else if (scale === '6-min') {
		const subsegment = segment / 10;
		return [
			100 - (segment * hour + subsegment),
			100 - (segment * hour + 2 * subsegment),
			100 - (segment * hour + 3 * subsegment),
			100 - (segment * hour + 4 * subsegment),
			100 - (segment * hour + 5 * subsegment),
			100 - (segment * hour + 6 * subsegment),
			100 - (segment * hour + 7 * subsegment),
			100 - (segment * hour + 8 * subsegment),
			100 - (segment * hour + 9 * subsegment),
		];
	}

	const subsegment = segment / 12;
	return [
		100 - (segment * hour + subsegment),
		100 - (segment * hour + 2 * subsegment),
		100 - (segment * hour + 3 * subsegment),
		100 - (segment * hour + 4 * subsegment),
		100 - (segment * hour + 5 * subsegment),
		100 - (segment * hour + 6 * subsegment),
		100 - (segment * hour + 7 * subsegment),
		100 - (segment * hour + 8 * subsegment),
		100 - (segment * hour + 9 * subsegment),
		100 - (segment * hour + 10 * subsegment),
		100 - (segment * hour + 11 * subsegment),
	];
}

function HourSegment(props: {
	date: CalendarDate;
	hour: number;
	scale?: SCALE;
	showText: boolean;
	workhours: Range<number>;
	hours: number[];
	actions?: CalendarActionItem[];
	onAction?: (key: React.Key, date: CalendarDate, time: number) => void;
}) {
	const segment = 100 / props.hours.length;
	const adjustedHour = props.hour - props.hours[0];
	const top = segment * adjustedHour;

	const borderTopColor: BorderColorValue =
		props.hour < props.workhours.min + 1 || props.hour > props.workhours.max - 1
			? 'gray-400'
			: 'gray-300';
	const borderBottomColor: BorderColorValue =
		props.hour < props.workhours.min || props.hour > props.workhours.max - 1
			? 'gray-400'
			: 'gray-300';

	const hourText = props.showText ? (
		<View width="size-600">
			<div
				id={'full-time-slot-' + props.hour}
				style={{ translate: '0px -1.7ex', textAlign: 'center' }}
			>
				<SpectrumText
					value={
						props.hour < 10 ? '0' + props.hour + ':00' : props.hour + ':00'
					}
					fontWeight={400}
					color={'gray-700'}
					UNSAFE_style={{
						backgroundColor:
							'var(--spectrum-alias-background-color-gray-50, var(--spectrum-global-color-gray-50, var(--spectrum-semantic-gray-50-color-background)))',
						paddingLeft: 2,
						paddingRight: 2,
					}}
				/>
			</div>
		</View>
	) : undefined;

	if (props.scale === 'hour') {
		const bottom = 100 - (segment * adjustedHour + segment);
		return (
			<SpectrumFlex
				borderTopWidth="thin"
				borderTopColor={borderTopColor}
				style={{
					position: 'absolute',
					top: top + '%',
					bottom: bottom + '%',
					width: '100%',
				}}
			>
				{!props.showText && (
					<ContextMenu
						date={props.date}
						time={props.hour}
						actions={props.actions}
						onAction={props.onAction}
					/>
				)}
				{hourText}
			</SpectrumFlex>
		);
	} else if (
		props.scale === 'quarter-hour' ||
		props.scale === '10-min' ||
		props.scale === '6-min' ||
		props.scale === '5-min'
	) {
		const bottoms = computeBottoms(adjustedHour, segment, props.scale);
		return (
			<>
				<SpectrumFlex
					borderBottomColor={borderBottomColor}
					borderBottomWidth="thin"
					borderTopWidth="thin"
					borderTopColor={borderTopColor}
					style={{
						borderBottomStyle: 'dashed',
						position: 'absolute',
						top: top + '%',
						bottom: bottoms[0] + '%',
						width: '100%',
					}}
				>
					{hourText}
				</SpectrumFlex>
				{bottoms
					.filter((_, idx) => idx !== 0)
					.map((v, idx) => {
						return (
							<SpectrumFlex
								key={'line-' + idx}
								borderBottomColor={borderBottomColor}
								borderBottomWidth="thin"
								style={{
									borderBottomStyle: 'dashed',
									position: 'absolute',
									top: top + '%',
									bottom: v + '%',
									width: '100%',
								}}
							></SpectrumFlex>
						);
					})}
			</>
		);
	}

	return (
		<>
			<SpectrumFlex
				borderBottomColor={borderBottomColor}
				borderBottomWidth="thin"
				borderTopWidth="thin"
				borderTopColor={borderTopColor}
				style={{
					borderBottomStyle: 'dashed',
					position: 'absolute',
					top: top + '%',
					height: segment / 2 + '%',
					width: '100%',
				}}
			>
				{!props.showText && (
					<ContextMenu
						date={props.date}
						time={props.hour}
						actions={props.actions}
						onAction={props.onAction}
					/>
				)}
				{hourText}
			</SpectrumFlex>
			<SpectrumFlex
				style={{
					position: 'absolute',
					top: top + segment / 2 + '%',
					height: segment / 2 + '%',
					width: '100%',
				}}
			>
				{!props.showText && (
					<ContextMenu
						date={props.date}
						time={props.hour + 0.5}
						actions={props.actions}
						onAction={props.onAction}
					/>
				)}
			</SpectrumFlex>
		</>
	);
}

function ContextMenu(props: {
	date: CalendarDate;
	time: number;
	actions?: CalendarActionItem[];
	onAction?: (key: React.Key, date: CalendarDate, time: number) => void;
}) {
	const [open, setOpen] = useState(false);

	if (!props.actions?.length) {
		return <></>;
	}

	return (
		<MenuTrigger
			trigger="longPress"
			align="start"
			direction="bottom"
			isOpen={open}
			onOpenChange={setOpen}
		>
			<Pressable>
				<div
					onContextMenu={(e) => {
						e.stopPropagation();
						e.preventDefault();
						setOpen(true);
					}}
					style={{ width: '100%' }}
				></div>
			</Pressable>
			<Menu
				onAction={(e) =>
					props.onAction && props.onAction(e, props.date, props.time)
				}
			>
				{props.actions.map((a) => (
					<Item key={a.key}>{a.label}</Item>
				))}
			</Menu>
		</MenuTrigger>
	);
}

function FullDayEntry(props: {
	startDate: CalendarDate;
	endDate: CalendarDate;
	maxLanes: number;
	layoutEntry: LaneLayoutEntry<SpectrumDayEvent>;
	noColoredEventText: boolean;
}) {
	const [open, setOpen] = useState(false);

	const top = (props.layoutEntry.lane.startLane / props.maxLanes) * 100 + '%';
	const bottom =
		'calc(' +
		(100 - ((props.layoutEntry.lane.startLane + 1) / props.maxLanes) * 100) +
		'% + 2px)';
	const left = 'calc(' + props.layoutEntry.dimension.min + '% + 4px)';
	const right = 'calc(' + (100 - props.layoutEntry.dimension.max) + '% + 3px)';

	const entry = props.layoutEntry.data;
	const className =
		'day-event type-' +
		(entry.canceled ? 'canceled' : entry.color) +
		(props.noColoredEventText ? ' uncolored' : '');
	const leadText =
		props.startDate.compare(entry.start) > 0
			? MONTH_AND_DAY.format(entry.start.toDate(getLocalTimeZone())).replace(
					/\./g,
					'',
				)
			: undefined;
	const endText =
		props.endDate.compare(entry.end) < 0
			? MONTH_AND_DAY.format(entry.end.toDate(getLocalTimeZone())).replace(
					/\./g,
					'',
				)
			: undefined;

	const eventEntry = (
		<div
			className={className}
			tabIndex={0}
			role="button"
			style={{
				position: 'absolute',
				display: 'flex',
				top,
				bottom,
				left,
				right,
				whiteSpace: 'nowrap',
				overflow: 'hidden',
			}}
			onContextMenu={(e) => {
				e.stopPropagation();
				e.preventDefault();
				setOpen(true);
			}}
		>
			<div
				style={{
					position: 'absolute',
					display: 'flex',
					left: 10,
					right: 10,
					top: 0,
					bottom: 0,
					alignItems: 'center',
				}}
			>
				{leadText && (
					<span
						style={{
							paddingRight: 5,
							fontWeight: 300,
							fontSize: '0.75em',
							display: 'inline-flex',
							alignItems: 'center',
						}}
					>
						<ChevronLeft size="XXS" /> {leadText}
					</span>
				)}
				<span style={{ fontWeight: 600 }}>{props.layoutEntry.data.title}</span>
				{endText && (
					<span
						style={{
							marginLeft: 'auto',
							fontWeight: 300,
							fontSize: '0.75em',
							display: 'inline-flex',
							alignItems: 'center',
						}}
					>
						{endText} <ChevronRight size="XXS" />
					</span>
				)}
			</div>
		</div>
	);

	if (entry.actions?.length) {
		return (
			<MenuTrigger
				direction="end"
				trigger="longPress"
				isOpen={open}
				onOpenChange={setOpen}
			>
				<Pressable>{eventEntry}</Pressable>
				<Menu onAction={(key) => entry.onAction && entry.onAction(key)}>
					{entry.actions.map((e) => (
						<Item key={e.key}>{e.label}</Item>
					))}
				</Menu>
			</MenuTrigger>
		);
	}

	return eventEntry;
}

function DayEntry(props: {
	layoutEntry: LaneLayoutEntry<SpectrumTimeEvent>;
	startShift: number;
	maxLanes: number;
	compactness: COMPACTNESS;
	noColoredEventText: boolean;
	moreInfoRenderer?: (eventEntry: SpectrumTimeEvent) => ReactNode | undefined;
}) {
	const eventDiv = useRef<HTMLDivElement>(null);
	const [fontSize, setFontSize] = useState('1.0em');
	const [open, setOpen] = useState(false);

	const entry = props.layoutEntry.data;
	const className =
		'event type-' +
		(entry.canceled ? 'canceled' : entry.color) +
		(props.noColoredEventText ? ' uncolored' : '');

	useResizeObserver({
		ref: eventDiv,
		onResize: () => {
			const div = eventDiv.current;
			if (div) {
				if (div.clientHeight < 20) {
					setFontSize('0.8em');
				} else {
					setFontSize('1em');
				}
			}
		},
	});

	const top =
		props.compactness === 'default'
			? `calc(${props.layoutEntry.dimension.min - props.startShift}% + 2px)`
			: `calc(${props.layoutEntry.dimension.min - props.startShift}% + 1px)`;

	const bottom =
		props.compactness === 'default'
			? `calc(${100 - props.layoutEntry.dimension.max + props.startShift}% + 2px)`
			: `calc(${100 - props.layoutEntry.dimension.max + props.startShift}% + 1px)`;

	const left = `${(props.layoutEntry.lane.startLane / props.layoutEntry.lane.maxLane) * 100}%`;

	const right =
		props.compactness === 'default'
			? `calc(max(${100 - (props.layoutEntry.lane.endLane / props.layoutEntry.lane.maxLane) * 100}% + 2px, 10px))`
			: `calc(max(${100 - (props.layoutEntry.lane.endLane / props.layoutEntry.lane.maxLane) * 100}% + 2px, 5px))`;

	const nodeMoreInfoData = props.layoutEntry.data.moreInfo ? (
		<div
			style={{
				display: 'grid',
				gridTemplateColumns: 'auto 1fr',
				columnGap: 10,
				rowGap: 5,
			}}
		>
			{props.layoutEntry.data.moreInfo.map((i) => (
				<>
					<span>{i.label}</span>
					<span>{i.content}</span>
				</>
			))}
		</div>
	) : undefined;

	const nodeMoreInfoRenderer = props.moreInfoRenderer
		? props.moreInfoRenderer(props.layoutEntry.data)
		: undefined;
	const moreInfoContent = nodeMoreInfoRenderer ?? nodeMoreInfoData;

	const eventEntry = (
		<div
			ref={eventDiv}
			className={className}
			tabIndex={0}
			role="button"
			id={'event-' + props.layoutEntry.data.key}
			style={{
				position: 'absolute',
				left,
				right,
				top,
				bottom,
				display: 'flex',
				scrollMarginTop: 10,
				scrollMarginBottom: 10,
			}}
			onContextMenu={(e) => {
				e.stopPropagation();
				e.preventDefault();
				setOpen(true);
			}}
		>
			<div
				style={{
					borderLeftStyle: 'solid',
					borderLeftWidth: 4,
					borderLeftColor: 'var(--event-color-fg)',
					borderTopLeftRadius: 2,
					borderBottomLeftRadius: 2,
				}}
			></div>
			<div
				className="event-text-positioner"
				style={{
					position: 'sticky',
					top: 0,
					display: 'flex',
					alignSelf: 'flex-start',
					fontSize: fontSize,
					lineHeight: props.compactness !== 'default' ? '1em' : '1.1em',
					flexGrow: 1,
				}}
			>
				<div
					className="event-text"
					style={{
						paddingLeft: props.compactness === 'default' ? 10 : 5,
						paddingRight: props.compactness === 'default' ? 5 : 2,
						borderRadius: 2,
						flexGrow: 1,
						display: 'flex',
						columnGap: props.compactness === 'default' ? 10 : 5,
						flexWrap: 'wrap',
						alignItems: 'center',
						overflow: 'hidden',
					}}
				>
					<span style={{ fontWeight: 600 }}>
						{props.layoutEntry.data.title}
					</span>
					<div style={{ display: 'inline-block', fontSize: '0.8em' }}>
						{TIME_FORMAT.format(entry.start.toDate(getLocalTimeZone()))} -{' '}
						{TIME_FORMAT.format(entry.end.toDate(getLocalTimeZone()))}
					</div>
					{moreInfoContent && (
						<DialogTrigger type="popover" placement="right">
							<Pressable>
								<button tabIndex={-1}>
									<HelpOutline size="XS" />
								</button>
							</Pressable>
							<Dialog size="S">
								<Heading>{entry.title}</Heading>
								<Divider></Divider>
								<SpectrumContent>{moreInfoContent}</SpectrumContent>
							</Dialog>
						</DialogTrigger>
					)}
				</div>
			</div>
		</div>
	);

	if (entry.actions?.length) {
		return (
			<MenuTrigger
				direction="end"
				trigger="longPress"
				isOpen={open}
				onOpenChange={setOpen}
			>
				<Pressable>{eventEntry}</Pressable>
				<Menu onAction={(key) => entry.onAction && entry.onAction(key)}>
					{entry.actions.map((e) => (
						<Item key={e.key}>{e.label}</Item>
					))}
				</Menu>
			</MenuTrigger>
		);
	}
	return eventEntry;
}

function computeDayLayout(
	startDate: CalendarDate,
	days: number,
	entries: readonly SpectrumDayEvent[],
): LaneLayout<LaneLayoutEntry<SpectrumDayEvent>> {
	return computeLaneLayout(
		entries.map((event) => toDayLayoutEntry(startDate, days, event)),
	);
}

function computeTimeLayout(
	date: CalendarDate,
	entries: readonly SpectrumTimeEvent[],
	hoursCount: number,
): LaneLayout<LaneLayoutEntry<SpectrumTimeEvent>> {
	return computeLaneLayout(
		entries.map((event) => toTimeLayoutEntry(date, event, hoursCount)),
	);
}

function toTimeFraction(time: Time, hoursCount: number) {
	const hour = 100 / hoursCount;
	const minutes = hour / 60;
	return hour * time.hour + minutes * time.minute;
}

function toTimeLayoutEntry(
	date: CalendarDate,
	e: SpectrumTimeEvent,
	hoursCount: number,
): LaneLayoutEntry<SpectrumTimeEvent> {
	const from = isSameDay(date, e.start) ? toTime(e.start) : parseTime('00:00');
	const to = isSameDay(date, e.end) ? toTime(e.end) : parseTime('24:00');
	return {
		data: e,
		dimension: {
			min: toTimeFraction(from, hoursCount),
			max: toTimeFraction(to, hoursCount),
		},
		lane: { startLane: 0, endLane: 1, maxLane: 1 },
	};
}

function toDayFraction(totalDays: number, day: number) {
	return (100 / totalDays) * day;
}

function toDayLayoutEntry(
	start: CalendarDate,
	totalDays: number,
	e: SpectrumDayEvent,
): LaneLayoutEntry<SpectrumDayEvent> {
	const from = Math.max(
		0,
		Duration.between(start, toCalendarDate(e.start)).toDays(),
	);
	const to = Math.min(
		totalDays,
		Duration.between(start, toCalendarDate(e.end)).toDays() + 1,
	);

	return {
		data: e,
		dimension: {
			min: toDayFraction(totalDays, from),
			max: toDayFraction(totalDays, to),
		},
		lane: { startLane: 0, endLane: 1, maxLane: 1 },
	};
}
