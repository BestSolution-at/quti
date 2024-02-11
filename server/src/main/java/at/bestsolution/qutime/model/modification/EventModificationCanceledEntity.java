package at.bestsolution.qutime.model.modification;

import at.bestsolution.qutime.model.EventModificationEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity( name = "EventModificationCanceled" )
@DiscriminatorValue(EventModificationEntity.DISCRIMINATOR_CANCELED)
public class EventModificationCanceledEntity extends EventModificationEntity {
    
}
