package at.bestsolution.qutime.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity(name="EventModification")
public abstract class EventModificationEntity {
    @Id
    @SequenceGenerator(name = "eventModSeq", sequenceName = "eventmod_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "eventModSeq")
    @Column( name = "em_id")
    public Long id;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "em_fk_event", foreignKey = @ForeignKey( name= "event_modification_event_fkey") )
    public EventEntity event;

    @Column( name = "em_date" )
    public LocalDate date;

    @SuppressWarnings("unchecked")
    public <T extends EventModificationEntity> T as(Class<T> clazz) {
        if( clazz == EventModificationCanceledEntity.class
            || clazz == EventModificationGenericEntity.class
            || clazz == EventModificationMovedEntity.class ) {
            if( getClass() == EventModificationCanceledEntity.class ) {
                return (T) this;
            } else if( getClass() == EventModificationGenericEntity.class ) {
                return (T) this;
            } else if( getClass() == EventModificationMovedEntity.class ) {
                return (T) this;
            }
            return null;
        }
        throw new IllegalArgumentException(String.format("Unsupported EventModificationEntity-Type '%s'", clazz));
    }
}