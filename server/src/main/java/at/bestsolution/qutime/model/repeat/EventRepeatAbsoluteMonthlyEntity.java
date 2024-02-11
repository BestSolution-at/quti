package at.bestsolution.qutime.model.repeat;

import at.bestsolution.qutime.model.EventRepeatEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity( name = "EventRepeatAbsoluteMonthly" )
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_ABS_MONTHLY)
public class EventRepeatAbsoluteMonthlyEntity extends EventRepeatEntity {
    @Column( name = "er_day_of_month")
    public short dayOfMonth;
}
