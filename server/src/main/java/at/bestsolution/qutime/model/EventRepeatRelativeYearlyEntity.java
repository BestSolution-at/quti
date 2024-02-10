package at.bestsolution.qutime.model;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EventRepeatRelativeYearlyEntity extends EventRepeatEntity {
    @Column( name = "er_days_of_week" )
    public List<DayOfWeek> daysOfWeek;

    @Column( name =  "er_month" )
    public Month month;
}
