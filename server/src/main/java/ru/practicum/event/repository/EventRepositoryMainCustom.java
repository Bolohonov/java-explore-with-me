package ru.practicum.event.repository;

import ru.practicum.event.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventRepositoryMainCustom implements EventRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Event> findEventsByState(String[] state) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);

        Path<String> statePath = event.get("state");

        List<Predicate> predicates = new ArrayList<>();
        for (String st : state) {
            predicates.add(cb.like(statePath, st));
        }
        query.select(event)
                .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }
}
