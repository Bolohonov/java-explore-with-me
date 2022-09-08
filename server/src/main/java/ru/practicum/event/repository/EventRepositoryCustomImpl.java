package ru.practicum.event.repository;

import ru.practicum.event.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.*;
import java.util.*;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Event> getEventsByStates(Set<Long> ids,
                                               Set<String> states,
                                               Set<Long> setOfCategories,
                                               Map<String, LocalDateTime> timeMap) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        query.select(event)
                .where(cb.or(getPredicates(cb, event, Long.class, ids, "initiatorId")),
                        cb.or(getPredicates(cb, event, String.class, states, "state")),
                        cb.or(getPredicates(cb, event, Long.class, setOfCategories, "category")),
                        cb.and(getTimePredicates(cb, event, timeMap)));
        return entityManager.createQuery(query)
                .getResultList();
    }

    private <T> Predicate[] getPredicates(CriteriaBuilder cb, Root<Event> event,
                                       Class <T> valueType, Set<T> set, String pathField) {
        List<Predicate> predicates = new ArrayList<>();
        if (!set.isEmpty()) {
            for (T value : set) {
                predicates.add(cb.or(cb.equal(event.get(pathField).as(valueType), value)));
            }
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Predicate[] getTimePredicates(CriteriaBuilder cb, Root<Event> event, Map<String, LocalDateTime> timeMap) {
        List<Predicate> predicates = new ArrayList<>();
        if (timeMap.containsKey("start")) {
            predicates.add(cb.greaterThan(event.get("eventDate").as(LocalDateTime.class), timeMap.get("start")));
        }
        if (timeMap.containsKey("end")) {
            predicates.add(cb.lessThan(event.get("eventDate").as(LocalDateTime.class), timeMap.get("end")));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
