import {
  CalendarDate,
  getLocalTimeZone,
  isSameDay,
  isToday,
  startOfMonth,
} from "@internationalized/date";
import { classesString, DAY, MONTH_AND_DAY, WEEK_DAY } from "../utils";

export function Header(props: { date: CalendarDate }) {
  const today = isToday(props.date, getLocalTimeZone());
  const isFirsDayOfMonth = isSameDay(startOfMonth(props.date), props.date);
  const date = props.date.toDate(getLocalTimeZone());

  return (
    <div class="day-header-container">
      <div
        class={classesString({
          "day-header-text-container": true,
          today: today,
        })}
      >
        <div class="day-header-text-node-container">
          <span class="day-text-date">
            {today || isFirsDayOfMonth
              ? MONTH_AND_DAY.format(date).replace(/\./g, "")
              : DAY.format(date)}
          </span>
          <span>{WEEK_DAY.format(date)}</span>
        </div>
      </div>
    </div>
  );
}
