// Generated by RSD - Do not modify
package at.bestsolution.quti.client.dto;

import java.time.LocalDate;
import java.time.ZoneId;

public interface EventRepeatDTO extends BaseDTO {
	public interface Patch {
	}

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

	public interface Builder extends BaseDTO.Builder {
		public Builder interval(short interval);

		public Builder endDate(LocalDate endDate);

		public Builder timeZone(ZoneId timeZone);

		public EventRepeatDTO build();
	}
}
