import { ReactNode, useRef, useState } from 'react';

import { getLocalTimeZone } from '@internationalized/date';

import { useResizeObserver } from '@react-aria/utils';
import { Pressable } from '@react-aria/interactions';
import HelpOutline from '@spectrum-icons/workflow/HelpOutline';

import { LaneLayoutEntry, TIME_FORMAT } from '../utils';
import { COMPACTNESS, QutiTimeEvent } from '../View.types';
import {
	Dialog,
	DialogTrigger,
	Divider,
	Heading,
	Content,
	MenuTrigger,
	Menu,
	Item,
} from '@adobe/react-spectrum';

export function DayEntry(props: {
	layoutEntry: LaneLayoutEntry<QutiTimeEvent>;
	startShift: number;
	maxLanes: number;
	compactness: COMPACTNESS;
	noColoredEventText: boolean;
	moreInfoRenderer?: (eventEntry: QutiTimeEvent) => ReactNode | undefined;
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
								<Content>{moreInfoContent}</Content>
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
