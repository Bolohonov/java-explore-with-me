package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.Event;
import ru.practicum.event.State;

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
    public Collection<Event> getEventsByStates(Set<String> states) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        for (String state : states) {
            predicates.add(cb.equal(event.get("state").as(String.class), state));
        }
        query.select(event)
                .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        return entityManager.createQuery(query)
                .getResultList();
    }
}
