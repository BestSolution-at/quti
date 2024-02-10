package at.bestsolution.qutime.model;

import java.time.DayOfWeek;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EventRepeatWeeklyEntity extends EventRepeatEntity {
    @Column( name = "er_days_of_week" )
    public List<DayOfWeek> daysOfWeek;
}
