package at.bestsolution.quti.service.jpa.model.repeat;

import at.bestsolution.quti.service.jpa.model.EventRepeatEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "EventRepeatDaily")
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_DAILY)
public class EventRepeatDailyEntity extends EventRepeatEntity {

}
