import {
	CalendarDate,
	CalendarDateTime,
	DateFormatter,
	getLocalTimeZone,
	toCalendarDate,
} from '@internationalized/date';

import { ColorValueV6 } from '@react-types/shared';
import { Pressable } from '@react-aria/interactions';

import { QutiDayEvent, QutiTimeEvent } from './View.types';
import { createDateRange, intersects } from './utils';
import { Grid, Item, Menu, MenuTrigger, View } from '@adobe/react-spectrum';
import { ExtendedText } from './generic/ExtendedText';
import { FlexView } from './generic/FlexView';
import { useState } from 'react';

export function QutiAgendaView(props: {
	dates: CalendarDate[];
	entries?: QutiTimeEvent[];
	fullDayEntries?: QutiDayEvent[];
}) {
	return (
		<Grid columns="6em 1fr" width="100%" flexGrow={1}>
			{props.dates.map((d) => (
				<QutiAgendaDay
					key={d.toString()}
					date={d}
					entries={props.entries}
					fullDayEntries={props.fullDayEntries}
				/>
			))}
		</Grid>
	);
}

function QutiAgendaDay(props: {
	date: CalendarDate;
	entries?: QutiTimeEvent[];
	fullDayEntries?: QutiDayEvent[];
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
				<ExtendedText
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
				<ExtendedText
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
				<FlexView
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
					<ExtendedText
						value="Keine Termine"
						fontSize="0.9em"
						fontWeight="bold"
						colorVersion={6}
						color="gray-500"
					/>
				</FlexView>
			)}
		</>
	);
}

function SpectrumAgendaEntry(props: { entry: QutiTimeEvent }) {
	const [open, setOpen] = useState(false);

	const backgroundColor = (props.entry.color + '-100') as ColorValueV6;
	const borderColor = (props.entry.color + '-1000') as ColorValueV6;
	const textColor = (props.entry.color + '-1100') as ColorValueV6;

	const eventEntry = (
		<FlexView
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
				<FlexView
					paddingY="size-50"
					paddingStart="size-100"
					alignItems="center"
				>
					<ExtendedText
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
				</FlexView>
				<FlexView paddingY="size-50" paddingEnd="size-100" alignItems="center">
					<ExtendedText
						color={textColor}
						colorVersion={6}
						fontWeight="bold"
						fontSize="0.9em"
						value={props.entry.title}
					/>
				</FlexView>
			</Grid>
		</FlexView>
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

function SpectrumAgendaDayEntry(props: { entry: QutiDayEvent }) {
	const backgroundColor = (props.entry.color + '-100') as ColorValueV6;
	const borderColor = (props.entry.color + '-1000') as ColorValueV6;
	const textColor = (props.entry.color + '-1100') as ColorValueV6;
	return (
		<FlexView
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
				<FlexView
					paddingY="size-50"
					paddingStart="size-100"
					alignItems="center"
				>
					<ExtendedText
						color={textColor}
						colorVersion={6}
						fontSize="0.7em"
						fontWeight="bold"
						value="ganztägig"
					/>
				</FlexView>
				<View paddingY="size-50" paddingEnd="size-100">
					<ExtendedText
						color={textColor}
						colorVersion={6}
						fontWeight="bold"
						fontSize="0.9em"
						value={props.entry.title}
					/>
				</View>
			</Grid>
		</FlexView>
	);
}
