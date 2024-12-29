// Generated by RSD - Do not modify
package at.bestsolution.quti.service.dto;

import java.time.LocalDate;
import java.time.ZoneId;

public interface MixinEventRepeatDataDTO {
    public interface Builder {}
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
}