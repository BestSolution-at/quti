package at.bestsolution.qutime.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity(name="Calendar")
public class CalendarEntity {
    @Id
    @SequenceGenerator(name = "calendarSeq", sequenceName = "calendar_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "calendarSeq")
    @Column( name = "ca_id" )
    public Long id;

    @Column( name = "ca_key", unique = true )
    public UUID key;

    @Column( name = "ca_name" )
    public String name;

    @Column( name = "ca_owner" )
    public String owner;

    @OneToMany( fetch=FetchType.LAZY, mappedBy="calendar", orphanRemoval = true )
    public List<EventEntity> events;
    
    @OneToMany( fetch=FetchType.LAZY, mappedBy="calendar", orphanRemoval = true )
    public List<EventReferenceEntity> eventReferences;
}
