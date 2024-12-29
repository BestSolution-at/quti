// Generated by RSD - Do not modify
package at.bestsolution.quti.service.dto;

import java.util.function.Consumer;
import java.util.function.Function;

public interface CalendarDTO extends BaseDTO, MixinCalendarDataDTO {
    /**
     * Unique identifier of the calendar
     */
    public String key();
    /**
     * Name of the calendar
     */
    public String name();
    /**
     * Owner of the calendar
     */
    public String owner();

    public interface Builder extends BaseDTO.Builder, MixinCalendarDataDTO.Builder {
        public Builder key(String key);
        public Builder name(String name);
        public Builder owner(String owner);
        public CalendarDTO build();
    }
    public interface Patch {
        public enum Props {
            NAME,
            OWNER,
        }

        public boolean isSet(Props prop);

        /**
         * Unique identifier of the calendar
         */
        public String key();
        /**
         * Name of the calendar
         */
        public String name();
        /**
         * Owner of the calendar
         */
        public String owner();
        public static void ifName(Patch dto, Consumer<String> consumer) {
            if( dto.isSet(Props.NAME) ) {
                consumer.accept(dto.name());
            }
        }
        public static <T> T ifName(Patch dto, Function<String, T> consumer, T defaultValue) {
            if( dto.isSet(Props.NAME) ) {
                return consumer.apply(dto.name());
            }
            return defaultValue;
        }
        public static void ifOwner(Patch dto, Consumer<String> consumer) {
            if( dto.isSet(Props.OWNER) ) {
                consumer.accept(dto.owner());
            }
        }
        public static <T> T ifOwner(Patch dto, Function<String, T> consumer, T defaultValue) {
            if( dto.isSet(Props.OWNER) ) {
                return consumer.apply(dto.owner());
            }
            return defaultValue;
        }
    }
}
