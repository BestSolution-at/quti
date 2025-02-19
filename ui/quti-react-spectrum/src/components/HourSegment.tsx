import { CalendarDate } from '@internationalized/date';

import { BorderColorValue } from '@react-types/shared';

import { QutiActionItem, Range, SCALE } from '../View.types';
import { Item, Menu, MenuTrigger, View } from '@adobe/react-spectrum';
import { ExtendedText } from '../generic/ExtendedText';
import { FlexView } from '../generic/FlexView';
import { computeBottoms } from '../utils';
import { useState } from 'react';
import { Pressable } from '@react-aria/interactions';

export function HourSegment(props: {
	date: CalendarDate;
	hour: number;
	scale?: SCALE;
	showText: boolean;
	workhours: Range<number>;
	hours: number[];
	actions?: QutiActionItem[];
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
				<ExtendedText
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
			<FlexView
				borderTopWidth="thin"
				borderTopColor={borderTopColor}
				UNSAFE_style={{
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
			</FlexView>
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
				<FlexView
					borderBottomColor={borderBottomColor}
					borderBottomWidth="thin"
					borderTopWidth="thin"
					borderTopColor={borderTopColor}
					UNSAFE_style={{
						borderBottomStyle: 'dashed',
						position: 'absolute',
						top: top + '%',
						bottom: bottoms[0] + '%',
						width: '100%',
					}}
				>
					{hourText}
				</FlexView>
				{bottoms
					.filter((_, idx) => idx !== 0)
					.map((v, idx) => {
						return (
							<View
								key={'line-' + idx}
								borderBottomColor={borderBottomColor}
								borderBottomWidth="thin"
								UNSAFE_style={{
									borderBottomStyle: 'dashed',
									position: 'absolute',
									top: top + '%',
									bottom: v + '%',
									width: '100%',
								}}
							></View>
						);
					})}
			</>
		);
	}

	return (
		<>
			<FlexView
				borderBottomColor={borderBottomColor}
				borderBottomWidth="thin"
				borderTopWidth="thin"
				borderTopColor={borderTopColor}
				UNSAFE_style={{
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
			</FlexView>
			<FlexView
				UNSAFE_style={{
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
			</FlexView>
		</>
	);
}

function ContextMenu(props: {
	date: CalendarDate;
	time: number;
	actions?: QutiActionItem[];
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
