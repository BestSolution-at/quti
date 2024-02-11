package at.bestsolution.qutime.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(EventRepeatEntity.DISCRIMINATOR_DAILY)
public class EventRepeatDailyEntity extends EventRepeatEntity {
    
}
