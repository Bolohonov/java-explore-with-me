package ru.practicum.event.repository;

import ru.practicum.event.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Event> getEventsByStates(Set<Long> ids, Set<String> states, Set<Long> setOfCategories) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        query.select(event)
                .where(cb.or(getPredicates(cb, event, Long.class, ids, "initiatorId")),
                        cb.or(getPredicates(cb, event, String.class, states, "state")),
                        cb.or(getPredicates(cb, event, Long.class, setOfCategories, "category")));
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
}
