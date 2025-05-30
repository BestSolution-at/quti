package at.bestsolution.quti.calendar.service.jpa.model.repeat;

import java.time.DayOfWeek;
import java.util.List;

import at.bestsolution.quti.calendar.service.jpa.model.EventRepeatEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "EventRepeatRelativeMonthly")
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_REL_MONTHLY)
public class EventRepeatRelativeMonthlyEntity extends EventRepeatEntity {
	@Column(name = "er_days_of_week")
	public List<DayOfWeek> daysOfWeek;
}
