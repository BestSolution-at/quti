package at.bestsolution.qutime.handler.calendar;

import java.util.UUID;

import at.bestsolution.qutime.model.CalendarEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CreateHandler {
    private final EntityManager em;

    @Inject
    public CreateHandler(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public String create(String name) {
        CalendarEntity c = new CalendarEntity();
        c.name = name;
        c.key = UUID.randomUUID();
        em.persist(c);
        return c.key.toString();
    }
}
