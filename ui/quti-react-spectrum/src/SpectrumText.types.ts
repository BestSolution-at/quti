import {
	ColorValue,
	ColorValueV6,
	ColorVersion,
	StyleProps,
} from '@react-types/shared';
import { ReactNode } from 'react';

type TextColor = {
	5: ColorValue;
	6: ColorValueV6;
};

export interface SpectrumTextProperties<T, C extends ColorVersion>
	extends StyleProps {
	value: T;
	formatter?: (val: T | undefined) => string;
	icon?: string | ReactNode;

	// attribute from 'TextProps' (@react-types/text) - not extended directly to avoid inclusion of 'children' attribute
	slot?: string;

	colorVersion?: C;

	// additional attributes, not yet supported by react-spectrum
	// see https://github.com/adobe/react-spectrum/issues/864
	color?: TextColor[C];
	whiteSpace?:
		| 'normal'
		| 'pre'
		| 'nowrap'
		| 'pre-wrap'
		| 'pre-line'
		| 'break-spaces';
	overflow?: 'clip' | 'ellipsis';
	fontWeight?: number | 'normal' | 'bold' | 'bolder' | 'lighter';
	align?: 'start' | 'end' | 'left' | 'right' | 'center';
	fontSize?: string | number;
}
