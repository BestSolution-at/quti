package at.bestsolution.qutime.model.repeat;

import at.bestsolution.qutime.model.EventRepeatEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_DAILY)
public class EventRepeatDailyEntity extends EventRepeatEntity {
    
}