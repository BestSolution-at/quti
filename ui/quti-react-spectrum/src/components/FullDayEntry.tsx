import { CalendarDate, getLocalTimeZone } from '@internationalized/date';

import { Pressable } from '@react-aria/interactions';
import ChevronLeft from '@spectrum-icons/workflow/ChevronLeft';
import ChevronRight from '@spectrum-icons/workflow/ChevronRight';

import { LaneLayoutEntry, MONTH_AND_DAY } from '../utils';
import { QutiDayEvent } from '../View.types';
import { useState } from 'react';
import { Item, Menu, MenuTrigger } from '@adobe/react-spectrum';

export function FullDayEntry(props: {
	startDate: CalendarDate;
	endDate: CalendarDate;
	maxLanes: number;
	layoutEntry: LaneLayoutEntry<QutiDayEvent>;
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
