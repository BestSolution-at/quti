package at.bestsolution.qutime.model.modification;

import java.time.ZonedDateTime;

import at.bestsolution.qutime.model.EventModificationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "EventModificationMoved")
@DiscriminatorValue(EventModificationEntity.DISCRIMINATOR_MOVED)
public class EventModificationMovedEntity extends EventModificationEntity {
	@Column(name = "em_start", nullable = false)
	public ZonedDateTime start;
	@Column(name = "em_end", nullable = false)
	public ZonedDateTime end;
}
