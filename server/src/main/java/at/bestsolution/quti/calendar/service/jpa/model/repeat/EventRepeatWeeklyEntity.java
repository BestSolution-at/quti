package at.bestsolution.quti.calendar.service.jpa.model.repeat;

import java.time.DayOfWeek;
import java.util.List;

import at.bestsolution.quti.calendar.service.jpa.model.EventRepeatEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "EventRepeatWeekly")
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_WEEKLY)
public class EventRepeatWeeklyEntity extends EventRepeatEntity {
	@Column(name = "er_days_of_week")
	public List<DayOfWeek> daysOfWeek;

	public void daysOfWeek(List<DayOfWeek> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
}
