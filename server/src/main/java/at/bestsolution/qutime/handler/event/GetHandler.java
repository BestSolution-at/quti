package at.bestsolution.qutime.handler.event;

import java.time.ZoneId;
import java.util.UUID;

import at.bestsolution.qutime.Utils;
import at.bestsolution.qutime.dto.EventDTO;
import at.bestsolution.qutime.model.EventEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.core.Response;

@Singleton
public class GetHandler {
    private final EntityManager em;

    @Inject
    public GetHandler(EntityManager em) {
        this.em = em;
    }

    public EventDTO get(UUID calendarKey, UUID eventKey, ZoneId zone) {
        try {
            var query = em.createQuery("FROM Event e WHERE e.key = :eventKey AND e.calendar.key = :calendarKey", EventEntity.class);
            query.setParameter("eventKey", eventKey);
            query.setParameter("calendarKey", calendarKey);
            var result = query.getSingleResult();
            return EventDTO.of(result, zone);
        } catch( NoResultException e ) {
            return null;
        }
    }
}
