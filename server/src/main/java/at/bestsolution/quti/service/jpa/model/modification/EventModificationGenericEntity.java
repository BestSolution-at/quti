package at.bestsolution.quti.service.jpa.model.modification;

import at.bestsolution.quti.service.jpa.model.EventModificationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "EventModificationGeneric")
@DiscriminatorValue(EventModificationEntity.DISCRIMINATOR_GENERIC)
public class EventModificationGenericEntity extends EventModificationEntity {
	@Column(name = "em_description")
	public String description;
}
