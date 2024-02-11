package at.bestsolution.qutime.model;

import java.time.DayOfWeek;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_REL_MONTHLY)
public class EventRepeatRelativeMonthlyEntity extends EventRepeatEntity {
    @Column( name = "er_days_of_week" )
    public List<DayOfWeek> daysOfWeek;
}
