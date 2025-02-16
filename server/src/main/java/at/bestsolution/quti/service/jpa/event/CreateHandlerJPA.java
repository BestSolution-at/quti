package at.bestsolution.quti.service.jpa.event;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.InvalidContentException;
import at.bestsolution.quti.service.Utils;
import at.bestsolution.quti.service.impl.EventServiceImpl;
import at.bestsolution.quti.service.jpa.BaseHandler;
import at.bestsolution.quti.service.jpa.calendar.utils.CalendarUtils;
import at.bestsolution.quti.service.jpa.event.utils.EventRepeatDTOUtil;
import at.bestsolution.quti.service.jpa.event.utils.EventUtils;
import at.bestsolution.quti.service.jpa.model.CalendarEntity;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.jpa.model.EventReferenceEntity;
import at.bestsolution.quti.service.model.EventNew;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Singleton
public class CreateHandlerJPA extends BaseHandler implements EventServiceImpl.CreateHandler {

	@Inject
	public CreateHandlerJPA(EntityManager em) {
		super(em);
	}

	private CalendarEntity calendar(UUID calendarKey) {
		return em().createQuery("FROM Calendar WHERE key = :key", CalendarEntity.class)
				.setParameter("key", calendarKey)
				.getSingleResult();
	}

	@Transactional
	public String create(BuilderFactory factory, String calendarKey, EventNew.Data event) {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");

		var em = em();
		var eventEntity = new EventEntity();
		eventEntity.calendar = calendar(parsedCalendarKey);
		eventEntity.key = UUID.randomUUID();
		eventEntity.title = event.title();
		eventEntity.description = event.description();
		eventEntity.start = event.fullday() ? Utils.atStartOfDay(event.start()) : event.start();
		eventEntity.end = event.fullday() ? Utils.atEndOfDay(event.end()) : event.end();
		eventEntity.fullday = event.fullday();
		eventEntity.tags = event.tags();

		if (event.repeat() != null) {
			var rv = EventRepeatDTOUtil.createRepeatPattern(event.start().toLocalDate(), event.repeat());
			eventEntity.repeatPattern = rv;
		}

		List<EventReferenceEntity> references = List.of();
		if (event.referencedCalendars() != null && event.referencedCalendars().size() > 0) {
			references = event.referencedCalendars().stream()
					.map(UUID::fromString)
					.map(key -> CalendarUtils.calendar(em, key))
					.filter(Objects::nonNull)
					.map(calendar -> {
						var ref = new EventReferenceEntity();
						ref.calendar = calendar;
						ref.event = eventEntity;
						return ref;
					}).toList();

			if (references.size() != event.referencedCalendars().size()) {
				throw new InvalidContentException("At least one calendar reference could not be resolved");
			}
		}

		EventUtils.validateEvent(eventEntity);

		if (eventEntity.repeatPattern != null) {
			em.persist(eventEntity.repeatPattern);
		}
		em.persist(eventEntity);

		references.forEach(em::persist);

		return eventEntity.key.toString();
	}
}
