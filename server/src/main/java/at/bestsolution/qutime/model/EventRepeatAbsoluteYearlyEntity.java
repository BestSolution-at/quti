package at.bestsolution.qutime.model;

import java.time.Month;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EventRepeatAbsoluteYearlyEntity extends EventRepeatEntity {
    @Column( name = "er_day_of_month" )
    public short dayOfMonth;
    @Column( name = "er_month" )
    public Month month;
}
