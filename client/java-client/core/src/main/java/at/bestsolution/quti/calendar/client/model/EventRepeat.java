// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.client.model;

import java.time.LocalDate;
import java.time.ZoneId;

public interface EventRepeat {
	public interface Data extends EventRepeat {
		public short interval();

		public LocalDate endDate();

		public ZoneId timeZone();

	}

	public interface DataBuilder {
	}
}
