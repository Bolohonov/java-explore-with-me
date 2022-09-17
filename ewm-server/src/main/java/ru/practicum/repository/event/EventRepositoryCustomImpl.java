package ru.practicum.repository.event;

import ru.practicum.model.event.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.*;
import java.util.*;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Event> getEvents(String text,
                                       Set<Long> setOfCategories,
                                       Boolean paid,
                                       Map<String, LocalDateTime> timeMap,
                                       Boolean onlyAvailable,
                                       String sort,
                                       Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        List<String> pathFields = Arrays.asList("annotation", "description");
        CriteriaQuery<Event> select = query.select(event)
                .where(cb.isNotNull(event.get("publishedOn")),
                        cb.or(getSearchPredicates(cb, event, pathFields, text)),
                        cb.or(getPredicatesEqual(cb, event, Long.class, setOfCategories, "category")),
                        getPredicateEqual(cb, event, Boolean.class, paid, "paid"),
                        cb.and(getTimePredicates(cb, event, timeMap)),
                        getAvailablePredicate(cb, event, onlyAvailable))
                .orderBy(cb.desc(event.get(sort)));

        return getResultWithPagination(cb, select, from, size);
    }

    @Override
    public Collection<Event> getEventsByAdmin(Set<Long> ids,
                                              Set<String> states,
                                              Set<Long> setOfCategories,
                                              Map<String, LocalDateTime> timeMap,
                                              Integer from,
                                              Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        CriteriaQuery<Event> select = query.select(event)
                .where(cb.or(getPredicatesEqual(cb, event, Long.class, ids, "initiatorId")),
                        cb.or(getPredicatesEqual(cb, event, String.class, states, "state")),
                        cb.or(getPredicatesEqual(cb, event, Long.class, setOfCategories, "category")),
                        cb.and(getTimePredicates(cb, event, timeMap)))
                .orderBy(cb.desc(event.get("eventDate")));

        return getResultWithPagination(cb, select, from, size);
    }

    private <T> Predicate[] getPredicatesEqual(CriteriaBuilder cb, Root<Event> event,
                                               Class<T> valueType, Set<T> set, String pathField) {
        List<Predicate> predicates = new ArrayList<>();
        if (!set.isEmpty()) {
            for (T value : set) {
                predicates.add(cb.equal(event.get(pathField).as(valueType), value));
            }
        } else {
            predicates.add(cb.conjunction());
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private <T> Predicate getPredicateEqual(CriteriaBuilder cb, Root<Event> event,
                                            Class<T> valueType, T value, String pathField) {
        if (value != null) {
            return cb.equal(event.get(pathField).as(valueType), value);
        }
        return cb.conjunction();
    }

    private Predicate[] getTimePredicates(CriteriaBuilder cb, Root<Event> event,
                                          Map<String, LocalDateTime> timeMap) {
        List<Predicate> predicates = new ArrayList<>();
        if (timeMap.isEmpty()) {
            predicates.add(cb.conjunction());
        }
        if (timeMap.containsKey("start")) {
            predicates.add(cb.greaterThan(event.get("eventDate").as(LocalDateTime.class), timeMap.get("start")));
        }
        if (timeMap.containsKey("end")) {
            predicates.add(cb.lessThan(event.get("eventDate").as(LocalDateTime.class), timeMap.get("end")));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Predicate getAvailablePredicate(CriteriaBuilder cb, Root<Event> event, Boolean onlyAvailable) {
        Predicate predicate = cb.conjunction();
        if (onlyAvailable != null && onlyAvailable.equals(Boolean.TRUE)) {
            predicate = cb.or(cb.equal(event.get("participantLimit"), 0),
                    cb.lessThan(event.get("confirmedRequests"), event.get("participantLimit")));
        }
        return predicate;
    }

    private Predicate[] getSearchPredicates(CriteriaBuilder cb,
                                            Root<Event> event,
                                            List<String> pathFields,
                                            String text) {
        List<Predicate> predicates = new ArrayList<>();
        if (text == null || pathFields.isEmpty()) {
            predicates.add(cb.conjunction());
        }
        for (String pathField : pathFields) {
            predicates.add(cb.like(cb.upper(event.get(pathField)),
                    cb.upper(cb.literal("%" + text + "%"))));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private <T> Long getCount(CriteriaBuilder cb, Class<T> valueType) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(valueType)));
        return entityManager.createQuery(countQuery)
                .getSingleResult();
    }

    private Collection<Event> getResultWithPagination(CriteriaBuilder cb,
                                                      CriteriaQuery<Event> select,
                                                      Integer from,
                                                      Integer size) {
        Collection<Event> resultList;
        int pageNumber = from % size;
        TypedQuery<Event> typedQuery = entityManager.createQuery(select);
        while (pageNumber < getCount(cb, Event.class).intValue()) {
            typedQuery.setFirstResult(from);
            typedQuery.setMaxResults(size);
            typedQuery.getResultList();
            pageNumber += size;
        }
        resultList = typedQuery.getResultList();
        return resultList;
    }
}
