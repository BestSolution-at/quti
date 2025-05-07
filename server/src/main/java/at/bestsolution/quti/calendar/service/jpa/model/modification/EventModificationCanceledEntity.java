package at.bestsolution.quti.calendar.service.jpa.model.modification;

import at.bestsolution.quti.calendar.service.jpa.model.EventModificationEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "EventModificationCanceled")
@DiscriminatorValue(EventModificationEntity.DISCRIMINATOR_CANCELED)
public class EventModificationCanceledEntity extends EventModificationEntity {

}
