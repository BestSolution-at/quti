import React from 'react';

import {
	parseDate,
	parseTime,
	toCalendarDateTime,
} from '@internationalized/date';

import type { Meta, StoryObj } from '@storybook/react';
import { SpectrumMultiDayView } from '../src/SpectrumDayView';

import { Provider, defaultTheme } from '@adobe/react-spectrum';

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories#default-export
const meta = {
	title: 'Example/MultiDayView',
	component: SpectrumMultiDayView,
	parameters: {
		// Optional parameter to center the component in the Canvas. More info: https://storybook.js.org/docs/configure/story-layout
		layout: 'centered',
	},
	// This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/writing-docs/autodocs
	tags: ['autodocs'],
	// More on argTypes: https://storybook.js.org/docs/api/argtypes
	argTypes: {},
	// Use `fn` to spy on the onClick arg, which will appear in the actions panel once invoked: https://storybook.js.org/docs/essentials/actions#action-args
	args: {},
	decorators: [
		(Story) => {
			return (
				<Provider theme={defaultTheme}>
					<div
						style={{
							overflow: 'hidden',
							minWidth: 900,
							maxWidth: 900,
							maxHeight: 800,
							display: 'flex',
						}}
					>
						<Story />
					</div>
				</Provider>
			);
		},
	],
} satisfies Meta<typeof SpectrumMultiDayView>;

export default meta;
type Story = StoryObj<typeof meta>;

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const Primary: Story = {
	args: {
		dates: [
			parseDate('2024-01-01'),
			parseDate('2024-01-02'),
			parseDate('2024-01-03'),
		],
		fullDayEntries: [
			{
				color: 'indigo',
				title: 'Vaction',
				key: '1',
				start: parseDate('2023-12-24'),
				end: parseDate('2024-01-06'),
			},
		],
		entries: [
			{
				color: 'blue',
				title: 'Sample Event',
				start: toCalendarDateTime(parseDate('2024-01-01'), parseTime('10:00')),
				end: toCalendarDateTime(parseDate('2024-01-01'), parseTime('12:00')),
				key: '1',
			},
		],
	},
};
