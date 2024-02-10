package at.bestsolution.qutime.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name="EventModificationMoved")
public class EventModificationMovedEntity extends EventModificationEntity {
    @Column( name = "em_start" )
    public ZonedDateTime start;
    @Column( name = "em_end")
    public ZonedDateTime end;
}