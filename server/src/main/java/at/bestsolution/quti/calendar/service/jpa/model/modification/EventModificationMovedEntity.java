package at.bestsolution.quti.calendar.service.jpa.model.modification;

import java.time.ZonedDateTime;

import at.bestsolution.quti.calendar.service.jpa.model.EventModificationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "EventModificationMoved")
@DiscriminatorValue(EventModificationEntity.DISCRIMINATOR_MOVED)
public class EventModificationMovedEntity extends EventModificationEntity {
	@Column(name = "em_start")
	public ZonedDateTime start;
	@Column(name = "em_end")
	public ZonedDateTime end;
}
