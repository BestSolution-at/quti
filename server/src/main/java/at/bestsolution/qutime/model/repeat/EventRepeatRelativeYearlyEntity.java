package at.bestsolution.qutime.model.repeat;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

import at.bestsolution.qutime.model.EventRepeatEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity( name = "EventRepeatRelativeYearly" )
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_REL_YEARLY)
public class EventRepeatRelativeYearlyEntity extends EventRepeatEntity {
    @Column( name = "er_days_of_week" )
    public List<DayOfWeek> daysOfWeek;

    @Column( name =  "er_month" )
    public Month month;
}
