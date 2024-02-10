package at.bestsolution.qutime.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public abstract class EventRepeatEntity {
    @Id
    @SequenceGenerator(name = "eventRepSeq", sequenceName = "eventrep_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "eventRepSeq")
    @Column( name = "er_id" )
    public Long id;

    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "er_fk_event", foreignKey = @ForeignKey( name = "event_repeat_event_fkey" ) )
    public EventEntity event;

    @Column( name = "er_interval", nullable = false )
    public short interval;

    @Column( name = "er_recurrence_tz", nullable = false )
    public ZoneId recurrenceTimezone;

    @Column( name = "er_start", nullable = false )
    public ZonedDateTime startDate;

    @Column( name = "er_end" )
    public ZonedDateTime endDate;
}
