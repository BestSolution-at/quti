package at.bestsolution.qutime.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EventModificationGenericEntity extends EventModificationEntity {
    @Column( name = "em_description" )
    public String description;
}
