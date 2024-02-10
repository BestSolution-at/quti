package at.bestsolution.qutime.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity(name="Event")
public class EventEntity {
    @Id
    @SequenceGenerator(name = "eventSeq", sequenceName = "event_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "eventSeq")
    @Column( name = "ev_id" )
    public Long id;

    @Column( name = "ev_key" )
    public UUID key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "ev_fk_calendar", foreignKey = @ForeignKey(name = "event_calendar_fkey") )
    public CalendarEntity calendar;

    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "ev_fk_repeat_pattern", foreignKey = @ForeignKey(name = "event_repeatpattern_fkey") )
    public EventRepeatEntity repeatPattern;

    @OneToMany( fetch = FetchType.LAZY, mappedBy="event", orphanRemoval = true )
    public List<EventReferenceEntity> references;

    @OneToMany( fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true )
    public List<EventModificationEntity> modifications;

    @Column( name = "ev_title" )
    public String title;
    @Column( name = "ev_description" )
    public String desription;
    @Column( name = "ev_start" )
    public ZonedDateTime start;
    @Column( name = "ev_end" )
    public ZonedDateTime end;
}
