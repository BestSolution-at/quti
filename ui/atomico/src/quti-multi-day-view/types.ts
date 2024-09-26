import { CalendarDateTime } from "@internationalized/date";

export type LocalDate = string;
export type LocalDatetime = string;

export type LocalDates = readonly LocalDate[];

export type EventEmitter<T> = (eventData: T) => void

export type QEvent = {
  readonly key: string;
  readonly start: LocalDatetime;
  readonly end: LocalDatetime;
  readonly subject: string;
  readonly fullday?: boolean;
  readonly type?: string;
};

export type QEvents = readonly QEvent[];

export type InternalEvent = {
  readonly key: string;
  readonly start: CalendarDateTime;
  readonly end: CalendarDateTime;
  readonly subject: string;
  readonly fullday?: boolean;
  readonly type?: string;
  readonly originalEvent: QEvent;
};

export type SCALE =
  | "hour"
  | "half-hour"
  | "quarter-hour"
  | "10-min"
  | "6-min"
  | "5-min";
