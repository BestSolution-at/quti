package at.bestsolution.qutime.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name="EventModificationCanceled")
@DiscriminatorValue(EventModificationEntity.DISCRIMINATOR_CANCELED)
public class EventModificationCanceledEntity extends EventModificationEntity {
    
}
