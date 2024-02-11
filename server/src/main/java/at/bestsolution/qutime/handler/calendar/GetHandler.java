package at.bestsolution.qutime.handler.calendar;

import java.util.UUID;

import at.bestsolution.qutime.dto.CalendarDTO;
import at.bestsolution.qutime.model.CalendarEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

@Singleton
public class GetHandler {
    private final EntityManager em;

    @Inject
    public GetHandler(EntityManager em) {
        this.em = em;
    }

    public CalendarDTO get(UUID key) { 
        var query = em.createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class);
        query.setParameter("key", key);
        var result = query.getResultList();
        if( result.size() == 1 ) {
            return CalendarDTO.of(result.get(0));
        } else if( result.size() == 0 ) {
            return null;
        }
        throw new IllegalStateException(String.format("Multiple calendars for '%s' are found"));
    }
}
