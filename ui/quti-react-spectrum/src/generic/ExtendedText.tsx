import { CSSProperties, ReactNode, useEffect, useState } from 'react';

import { ColorValue, ColorValueV6, ColorVersion } from '@react-types/shared';
import {
	StyleHandlers,
	baseStyleProps,
	useStyleProps,
} from '@react-spectrum/utils';
import { Text } from '@adobe/react-spectrum';
import { ExtendedTextProperties } from './ExtendedText.types';

// reuse React baseStyleProps, but remove attributes which are supported and forwarded directly
// to avoid adding duplicates in the UNSAFE_style
const textStyleProps: StyleHandlers = Object.keys(baseStyleProps).reduce(
	(object, key) => {
		if (!['flex', 'flexBasis', 'flexGrow', 'flexShrink'].includes(key)) {
			object[key] = baseStyleProps[key];
		}
		return object;
	},
	{} as StyleHandlers,
);

export function ExtendedText<T, C extends ColorVersion = 5>(
	props: ExtendedTextProperties<T, C>,
): ReactNode {
	const { styleProps } = useStyleProps(props, textStyleProps);

	const [value, setValue] = useState(props.value);
	const textColor: CSSProperties = {
		color: props.color
			? props.colorVersion === 5 || props.colorVersion === undefined
				? spectrumColorToCssValue(props.color as ColorValue)
				: spectrumColorToCssValue6(props.color as ColorValueV6)
			: undefined,
	};
	const overflowStyle: CSSProperties = {
		textOverflow: props.overflow,
		overflow: props.overflow ? 'hidden' : undefined,
	};

	const finalStyles: CSSProperties = {
		whiteSpace: props.whiteSpace,
		fontWeight: props.fontWeight,
		fontSize: props.fontSize,
		textAlign: props.align,
		...textColor,
		...overflowStyle,
		...styleProps.style,
	};

	useEffect(() => {
		setValue(props.value);
		return undefined;
	}, [props.value]);

	const icon = props.icon;

	return (
		<Text
			flex={props.flex}
			flexBasis={props.flexBasis}
			flexGrow={props.flexGrow}
			flexShrink={props.flexShrink}
			slot={props.slot}
			UNSAFE_style={finalStyles}
		>
			{icon && <>{icon} </>}
			{props.formatter ? (
				props.formatter(value)
			) : value !== undefined ? (
				String(value)
			) : (
				<></>
			)}
		</Text>
	);
}

// setting a custom text color in react-spectrum is not yet supported
// -> use workaround to be able to use 'official' spectrum colors, based on react-spectrum's internal implementation
function spectrumColorToCssValue(color: ColorValue): string {
	// see https://github.com/adobe/react-spectrum/blob/61de930401cac9868ac18a9a475ea1cda9453356/packages/@react-spectrum/utils/src/styleProps.ts#L134-L136
	return `var(--spectrum-alias-text-color-${color}, var(--spectrum-global-color-${color}, var(--spectrum-semantic-${color}-color-default)))`;
}

function spectrumColorToCssValue6(color: ColorValueV6): string {
	// see https://github.com/adobe/react-spectrum/blob/61de930401cac9868ac18a9a475ea1cda9453356/packages/@react-spectrum/utils/src/styleProps.ts#L134-L136
	return `var(--spectrum-alias-text-color-${color}, var(--spectrum-${color}, var(--spectrum-semantic-${color}-color-default)))`;
}
