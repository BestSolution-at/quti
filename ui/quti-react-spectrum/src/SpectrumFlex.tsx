import {
	CSSProperties,
	JSX,
	Children as ReactChildren,
	ReactElement,
	ReactNode,
	PropsWithChildren as ReactPropsWithChildren,
	forwardRef,
	useCallback,
	useRef,
	useState,
} from 'react';

import { ColorVersion, DOMRef, DOMRefValue } from '@react-types/shared';
import {
	useDOMRef,
	useStyleProps,
	useUnwrapDOMRef,
	viewStyleProps,
} from '@react-spectrum/utils';
import { Flex } from '@adobe/react-spectrum';
import { useResizeObserver } from '@react-aria/utils';

import {
	SpectrumFlexProperties,
	SpectrumResponsiveItemProps,
} from './SpectrumFlex.types';

export function SpectrumFlex<C extends ColorVersion = 5>(
	props: SpectrumFlexProperties<C>,
): JSX.Element {
	const { styleProps } = useStyleProps(props, viewStyleProps);

	const finalStyles: CSSProperties = {
		boxShadow: props.boxShadow,
		...styleProps.style,
		...props.style,
	};

	if (
		Object.keys(finalStyles).length !== 0 &&
		(props.rowGap || props.columnGap || props.gap) &&
		!isFlexGapSupported()
	) {
		finalStyles.display = props.isHidden ? 'none' : 'flex';
		finalStyles.flexDirection = 'row';
		if (props.isResponsive) {
			return (
				<SafariResponsiveSpectrumFlex
					key="safari-responsive"
					{...props}
					_wrapperStyles={finalStyles}
				/>
			);
		}
		return (
			<SafariSpectrumFlex
				key="safari-wrap"
				{...props}
				_wrapperStyles={finalStyles}
			/>
		);
	}

	if (props.isResponsive) {
		return (
			<DefaultResponsiveSpectrumFlex
				key="default-responsive"
				{...props}
				_wrapperStyles={finalStyles}
			/>
		);
	}
	return (
		<DefaultSpectrumFlex
			key="default-flex"
			{...props}
			_wrapperStyles={finalStyles}
		/>
	);
}

function _DefaultSpectrumFlex<C extends ColorVersion = 5>(
	props: SpectrumFlexProperties<C> & { _wrapperStyles?: CSSProperties },
	ref: DOMRef<HTMLDivElement>,
) {
	return (
		<Flex
			ref={ref}
			id={props.id}
			direction={props.direction}
			columnGap={props.columnGap}
			rowGap={props.rowGap}
			gap={props.gap}
			isHidden={props.isHidden}
			wrap={props.wrap}
			minWidth={props.fixedWidth}
			maxWidth={props.fixedWidth}
			justifyContent={props.justifyContent}
			alignItems={props.alignItems}
			alignContent={props.alignContent}
			UNSAFE_style={props._wrapperStyles}
			UNSAFE_className={props.UNSAFE_className}
		>
			<>{props.children}</>
		</Flex>
	);
}

const DefaultSpectrumFlex = forwardRef(_DefaultSpectrumFlex);

function DefaultResponsiveSpectrumFlex<C extends ColorVersion = 5>(
	props: SpectrumFlexProperties<C> & { _wrapperStyles?: CSSProperties },
) {
	const [filteredChildren, setFilteredChildren] = useState<ReactNode>();
	const div = useRef<DOMRefValue<HTMLDivElement>>(null);
	const unwrappedDiv = useUnwrapDOMRef(div);

	const onResize = useCallback(() => {
		if (unwrappedDiv.current === null) {
			return;
		}
		const width = unwrappedDiv.current.clientWidth;
		if (width > 0) {
			const newChildren = getResponsiveChildren(props.children, width);
			setFilteredChildren(newChildren);
		}
	}, [unwrappedDiv, props.children]);

	useResizeObserver({
		ref: unwrappedDiv,
		onResize,
	});

	return (
		<DefaultSpectrumFlex ref={div} {...props} children={filteredChildren} />
	);
}

function getResponsiveChildren(children: ReactNode, width: number) {
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	const list: any[] = [];
	ReactChildren.forEach(children, (e) => list.push(e));

	return list.filter((e) => {
		if (e !== null && e.type._typeName === 'SpectrumResponsiveItem') {
			return inRange(
				width,
				(e as ReactElement<SpectrumResponsiveItemProps>).props.widthRange,
			);
		}
		return true;
	});
}

function inRange(width: number, range: [minWidth: number, maxWidth?: number]) {
	if (width >= range[0] && (range[1] === undefined || width <= range[1])) {
		return true;
	}
	return false;
}

function _SafariSpectrumFlex<C extends ColorVersion = 5>(
	props: SpectrumFlexProperties<C> & { _wrapperStyles?: CSSProperties },
	ref: DOMRef<HTMLDivElement>,
) {
	const domRef = useDOMRef(ref);
	const propsCopy = { ...props };

	return (
		<div
			key="safari-wrapper"
			ref={domRef}
			style={props._wrapperStyles}
			id="safari-wrapper"
		>
			<DefaultSpectrumFlex {...propsCopy} _wrapperStyles={{ width: '100%' }} />
		</div>
	);
}

const SafariSpectrumFlex = forwardRef(_SafariSpectrumFlex);

function SafariResponsiveSpectrumFlex<C extends ColorVersion = 5>(
	props: SpectrumFlexProperties<C> & { _wrapperStyles?: CSSProperties },
) {
	const [filteredChildren, setFilteredChildren] = useState<ReactNode>();
	const div = useRef<DOMRefValue<HTMLDivElement>>(null);
	const unwrappedDiv = useUnwrapDOMRef(div);

	const onResize = useCallback(() => {
		if (unwrappedDiv.current === null) {
			return;
		}
		const width = unwrappedDiv.current.clientWidth;
		if (width > 0) {
			const newChildren = getResponsiveChildren(props.children, width);
			console.log('children', newChildren);
			setFilteredChildren(newChildren);
		}
	}, [unwrappedDiv, props.children]);

	useResizeObserver({
		ref: unwrappedDiv,
		onResize,
	});

	return (
		<SafariSpectrumFlex ref={div} {...props} children={filteredChildren} />
	);
}

// Original licensing for the following method can be found in the
// NOTICE file in the root directory of this source tree.
// See https://github.com/Modernizr/Modernizr/blob/7efb9d0edd66815fb115fdce95fabaf019ce8db5/feature-detects/css/flexgap.js

let _isFlexGapSupported: boolean | null = null;
function isFlexGapSupported() {
	if (_isFlexGapSupported != null) {
		return _isFlexGapSupported;
	}

	if (typeof document === 'undefined') {
		return false;
	}

	// create flex container with row-gap set
	const flex = document.createElement('div');
	flex.style.display = 'flex';
	flex.style.flexDirection = 'column';
	flex.style.rowGap = '1px';

	// create two, elements inside it
	flex.appendChild(document.createElement('div'));
	flex.appendChild(document.createElement('div'));

	// append to the DOM (needed to obtain scrollHeight)
	document.body.appendChild(flex);
	_isFlexGapSupported = flex.scrollHeight === 1; // flex container should be 1px high from the row-gap
	flex.parentNode?.removeChild(flex);

	return _isFlexGapSupported;
}

export function SpectrumResponsiveItem(
	props: ReactPropsWithChildren<SpectrumResponsiveItemProps>,
): JSX.Element {
	return <>{props.children}</>;
}

SpectrumResponsiveItem._typeName = 'SpectrumResponsiveItem';
