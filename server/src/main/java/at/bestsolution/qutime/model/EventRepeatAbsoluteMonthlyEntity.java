package at.bestsolution.qutime.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EventRepeatAbsoluteMonthlyEntity extends EventRepeatEntity {
    @Column( name = "er_day_of_month")
    public short dayOfMonth;
}
