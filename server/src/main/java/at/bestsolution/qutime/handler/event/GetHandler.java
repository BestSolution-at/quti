package at.bestsolution.qutime.handler.event;

import java.time.ZoneId;
import java.util.UUID;

import at.bestsolution.qutime.dto.EventDTO;
import at.bestsolution.qutime.model.EventEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class GetHandler {
    private final EntityManager em;

    @Inject
    public GetHandler(EntityManager em) {
        this.em = em;
    }

    public EventDTO get(UUID calendarKey, UUID eventKey, ZoneId zone) {
        
        var query = em.createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey", EventEntity.class);
        query.setParameter("eventKey", eventKey);
        query.setParameter("calendarKey", calendarKey);
        var result = query.getResultList();
        if( result.size() == 1 ) {
            return EventDTO.of(result.get(0), zone);
        } else if( result.size() == 0 ) {
            return null;
        }
        throw new IllegalStateException(String.format("Multiple matching events for '%s' are found.", eventKey));
    }
}
