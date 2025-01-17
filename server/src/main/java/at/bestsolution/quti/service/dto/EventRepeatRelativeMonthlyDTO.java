// Generated by RSD - Do not modify
package at.bestsolution.quti.service.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.List;

public interface EventRepeatRelativeMonthlyDTO extends EventRepeatDTO, MixinEventRepeatDataDTO {
    /**
     * selects the first day in a month
     */
    public List<DayOfWeek> daysOfWeek();

    public interface Builder extends EventRepeatDTO.Builder, MixinEventRepeatDataDTO.Builder {
        public Builder daysOfWeek(List<DayOfWeek> daysOfWeek);
        public Builder interval(short interval);
        public Builder endDate(LocalDate endDate);
        public Builder timeZone(ZoneId timeZone);
        public EventRepeatRelativeMonthlyDTO build();
    }
    public interface Patch extends EventRepeatDTO.Patch {
        public enum Props {
            DAYSOFWEEK,
            INTERVAL,
            ENDDATE,
            TIMEZONE,
        }

        public boolean isSet(Props prop);

        /**
         * selects the first day in a month
         */
        public List<DayOfWeek> daysOfWeek();
        /**
         * Repeat interval
         */
        public short interval();
        /**
         * End date of the repeat
         */
        public LocalDate endDate();
        /**
         * Timezone in which the event repeats
         */
        public ZoneId timeZone();
        public static void ifDaysOfWeek(Patch dto, Consumer<List<DayOfWeek>> consumer) {
            if( dto.isSet(Props.DAYSOFWEEK) ) {
                consumer.accept(dto.daysOfWeek());
            }
        }
        public static <T> T ifDaysOfWeek(Patch dto, Function<List<DayOfWeek>, T> consumer, T defaultValue) {
            if( dto.isSet(Props.DAYSOFWEEK) ) {
                return consumer.apply(dto.daysOfWeek());
            }
            return defaultValue;
        }
        public static void ifInterval(Patch dto, Consumer<Short> consumer) {
            if( dto.isSet(Props.INTERVAL) ) {
                consumer.accept(dto.interval());
            }
        }
        public static <T> T ifInterval(Patch dto, Function<Short, T> consumer, T defaultValue) {
            if( dto.isSet(Props.INTERVAL) ) {
                return consumer.apply(dto.interval());
            }
            return defaultValue;
        }
        public static void ifEndDate(Patch dto, Consumer<LocalDate> consumer) {
            if( dto.isSet(Props.ENDDATE) ) {
                consumer.accept(dto.endDate());
            }
        }
        public static <T> T ifEndDate(Patch dto, Function<LocalDate, T> consumer, T defaultValue) {
            if( dto.isSet(Props.ENDDATE) ) {
                return consumer.apply(dto.endDate());
            }
            return defaultValue;
        }
        public static void ifTimeZone(Patch dto, Consumer<ZoneId> consumer) {
            if( dto.isSet(Props.TIMEZONE) ) {
                consumer.accept(dto.timeZone());
            }
        }
        public static <T> T ifTimeZone(Patch dto, Function<ZoneId, T> consumer, T defaultValue) {
            if( dto.isSet(Props.TIMEZONE) ) {
                return consumer.apply(dto.timeZone());
            }
            return defaultValue;
        }
    }
}
