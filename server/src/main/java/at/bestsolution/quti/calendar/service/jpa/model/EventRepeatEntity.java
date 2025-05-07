package at.bestsolution.quti.calendar.service.jpa.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "EventRepeat")
@DiscriminatorColumn(name = "er_type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class EventRepeatEntity {
	public static final String DISCRIMINATOR_ABS_MONTHLY = "1";
	public static final String DISCRIMINATOR_ABS_YEAR = "2";
	public static final String DISCRIMINATOR_DAILY = "3";
	public static final String DISCRIMINATOR_REL_MONTHLY = "4";
	public static final String DISCRIMINATOR_REL_YEARLY = "5";
	public static final String DISCRIMINATOR_WEEKLY = "6";

	@Id
	@SequenceGenerator(name = "eventrep_seq", sequenceName = "eventrep_seq_id", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "eventrep_seq")
	@Column(name = "er_id")
	public Long id;

	@Column(name = "er_interval", nullable = false)
	public short interval;

	@Column(name = "er_recurrence_tz", nullable = false)
	public ZoneId recurrenceTimezone;

	@Column(name = "er_start", nullable = false)
	public ZonedDateTime startDate;

	@Column(name = "er_end")
	public ZonedDateTime endDate;

	public void interval(short interval) {
		this.interval = interval;
	}

	public void recurrenceTimezone(ZoneId recurrenceTimezone) {
		this.recurrenceTimezone = recurrenceTimezone;
	}

	public void startDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public void endDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}

}
