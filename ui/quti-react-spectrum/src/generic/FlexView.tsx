import { Flex, FlexProps, ViewProps } from '@adobe/react-spectrum';
import { ColorVersion } from '@react-types/shared';
import { PropsWithChildren } from 'react';

import { useStyleProps, viewStyleProps } from '@react-spectrum/utils';

export function FlexView<C extends ColorVersion = 5>(
	props: PropsWithChildren<ViewProps<C> & FlexProps>,
) {
	const { children, ...otherProps } = props;
	const { styleProps } = useStyleProps(otherProps, viewStyleProps);
	const style = {
		...styleProps.style,
		...props.UNSAFE_style,
	};
	return (
		<Flex {...props} UNSAFE_style={style}>
			{children}
		</Flex>
	);
}
