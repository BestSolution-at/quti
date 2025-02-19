import { CSSProperties, ReactNode } from 'react';

import { ColorVersion, DimensionValue, FlexStyleProps, ViewStyleProps } from '@react-types/shared';

export interface SpectrumFlexProperties<C extends ColorVersion = 5> extends ViewStyleProps<C>, FlexStyleProps {
  id?: string;
  isHidden?: boolean;

  fixedWidth?: number | string;
  boxShadow?: string

  direction?: 'row' | 'column' | 'row-reverse' | 'column-reverse';
  columnGap?: DimensionValue;
  rowGap?: DimensionValue;

  style?: CSSProperties;
  children?: ReactNode;
  isResponsive?: boolean
}

export interface SpectrumResponsiveItemProps {
  widthRange: [ minWidth: number, maxWidth?: number ]
}
