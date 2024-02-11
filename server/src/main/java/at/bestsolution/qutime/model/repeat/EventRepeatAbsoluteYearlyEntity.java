package at.bestsolution.qutime.model.repeat;

import java.time.Month;

import at.bestsolution.qutime.model.EventRepeatEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_ABS_YEAR)
public class EventRepeatAbsoluteYearlyEntity extends EventRepeatEntity {
    @Column( name = "er_day_of_month" )
    public short dayOfMonth;
    @Column( name = "er_month" )
    public Month month;
}
