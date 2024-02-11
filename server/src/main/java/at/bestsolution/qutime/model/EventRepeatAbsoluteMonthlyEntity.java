package at.bestsolution.qutime.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_ABS_MONTHLY)
public class EventRepeatAbsoluteMonthlyEntity extends EventRepeatEntity {
    @Column( name = "er_day_of_month")
    public short dayOfMonth;
}
