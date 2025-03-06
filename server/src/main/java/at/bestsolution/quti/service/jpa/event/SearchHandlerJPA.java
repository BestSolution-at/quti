package at.bestsolution.quti.service.jpa.event;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import at.bestsolution.quti.service.BuilderFactory;
import at.bestsolution.quti.service.InvalidArgumentException;
import at.bestsolution.quti.service.Utils;
import at.bestsolution.quti.service.impl.EventServiceImpl;
import at.bestsolution.quti.service.jpa.BaseReadonlyHandler;
import at.bestsolution.quti.service.jpa.event.utils.EventDTOUtil;
import at.bestsolution.quti.service.jpa.model.EventEntity;
import at.bestsolution.quti.service.model.Event;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import at.bestsolution.quti.service.model.EventSearch;

@Singleton
public class SearchHandlerJPA extends BaseReadonlyHandler implements EventServiceImpl.SearchHandler {
	@Inject
	public SearchHandlerJPA(EntityManager em) {
		super(em);
	}

	@Override
	public List<Event.Data> search(
			BuilderFactory factory,
			String calendarKey,
			EventSearch.Data filter,
			ZoneId timezone)
			throws InvalidArgumentException {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");

		var tags = filter.tags();
		var startRange = filter.startRange();
		var endRange = filter.endRange();
		var filters = new ArrayList<String>();

		var params = new HashMap<String, Object>();
		params.put("calendarKey", parsedCalendarKey);

		if (tags.size() > 0) {
			params.put("tags", tags.toArray(new String[0]));
			filters.add("AND array_overlaps(e.tags, :tags)");
		}

		if (startRange != null) {
			var range = startRange;
			var min = range.min();
			var max = range.max();
			if (min != null && max != null) {
				params.put("startMin", min);
				params.put("startMax", max);
				filters.add("AND start BETWEEN :startMin AND :startMax");
			} else if (min != null) {
				params.put("startMin", min);
				filters.add("AND start >= :startMin");
			} else {
				params.put("startMax", max);
				filters.add("AND start <= :startMax");
			}
		}

		if (endRange != null) {
			var range = endRange;
			var min = range.min();
			var max = range.max();
			if (min != null && max != null) {
				params.put("endMin", min);
				params.put("endMax", max);
				filters.add("AND end BETWEEN :endMin AND :endMax");
			} else if (min != null) {
				params.put("endMin", min);
				filters.add("AND end >= :endMin");
			} else {
				params.put("endMax", max);
				filters.add("AND end <= :endMax");
			}
		}

		var filterString = filters.stream().collect(Collectors.joining("\n"));

		System.err.println(params);
		var q = em().createQuery("""
				SELECT
					e
				FROM
					Event e
				WHERE
					e.calendar.key = :calendarKey
				%s
				""".formatted(filterString), EventEntity.class);
		params.forEach(q::setParameter);
		// q.setPa

		return q.getResultList()
				.stream()
				.map(e -> EventDTOUtil.of(factory, e, timezone == null ? ZoneOffset.UTC : timezone))
				.toList();
	}

	public List<Event.Data> _search(
			BuilderFactory factory,
			String calendarKey,
			EventSearch.Data filter,
			ZoneId timezone)
			throws InvalidArgumentException {
		var parsedCalendarKey = Utils.parseUUID(calendarKey, "in path");

		var cb = em().getCriteriaBuilder();
		var cr = cb.createQuery(EventEntity.class);
		var root = cr.from(EventEntity.class);

		var predicates = new ArrayList<Predicate>();

		var tags = filter.tags();
		var startRange = filter.startRange();
		var endRange = filter.endRange();
		// var fullday = filter.fullday();

		predicates.add(cb.equal(root.get("calendar").get("key"), parsedCalendarKey));

		if (tags.size() > 0) {
			var path = root.get("tags");
			var predicat = cb.isTrue(
					cb.function(
							"array_contains",
							Boolean.class,
							cb.function("array", String[].class, path),
							((HibernateCriteriaBuilder) cb).arrayLiteral(tags.toArray(String[]::new))));
			predicates.add(predicat);
		}

		if (startRange != null) {
			var range = startRange;
			var min = range.min();
			var max = range.max();
			if (min != null && max != null) {
				predicates.add(cb.between(root.get("start"), min, max));
			} else if (min != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("start"), min));
			} else if (max != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("start"), max));
			}
		}

		if (endRange != null) {
			var range = endRange;
			var min = range.min();
			var max = range.max();
			if (min != null && max != null) {
				predicates.add(cb.between(root.get("end"), min, max));
			} else if (min != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("end"), min));
			} else if (max != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("end"), max));
			}
		}

		var q = em().createQuery(cr.select(root).where(predicates.toArray(new Predicate[0])));
		return q.getResultList()
				.stream()
				.map(e -> EventDTOUtil.of(factory, e, timezone == null ? ZoneOffset.UTC : timezone))
				.toList();
	}
}
