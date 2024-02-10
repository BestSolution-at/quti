package at.bestsolution.qutime.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity(name="EventReference")
public class EventReferenceEntity {
    @Id
    @SequenceGenerator(name = "eventRefSeq", sequenceName = "eventref_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "eventRefSeq")
    @Column( name = "er_id" )
    public Long id;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "er_fk_calendar", foreignKey = @ForeignKey(name = "event_reference_calendar_fkey") )
    public CalendarEntity calendar;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name= "er_fk_event", foreignKey = @ForeignKey(name = "event_reference_event_fkey") )
    public EventEntity event;
}
