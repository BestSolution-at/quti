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
        try {
            var query = em.createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class);
            query.setParameter("key", key);
            var result = query.getSingleResult();
            return CalendarDTO.of(result);
        } catch(NoResultException e) {
            return null;
        }
    }
}
