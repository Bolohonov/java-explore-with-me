package ru.practicum.repository;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

public class EndpointHitRepositoryCustomImpl implements EndpointHitRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EndpointHit> getEndpointHits(Long start, Long end, List<String> uris) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EndpointHit> query = cb.createQuery(EndpointHit.class);
        Root<EndpointHit> eHit = query.from(EndpointHit.class);
        CriteriaQuery<EndpointHit> select = query.select(eHit)
                .where(cb.equal(eHit.get("app"), "ExploreWithMe"),
                        cb.and(getTimePredicates(cb, eHit, start, end)),
                        cb.or(getPredicatesEqual(cb, eHit, String.class, new HashSet<>(uris), "uri"))
                );
        return entityManager.createQuery(select).getResultList();
    }

    private Predicate[] getTimePredicates(CriteriaBuilder cb, Root<EndpointHit> eHit,
                                          Long start, Long end) {
        List<Predicate> predicates = new ArrayList<>();
        if (start == null && end == null) {
            predicates.add(cb.conjunction());
        }
        if (start != null) {
            predicates.add(cb.greaterThan(eHit.get("timestamp").as(Long.class), start));
        }
        if (end != null) {
            predicates.add(cb.lessThan(eHit.get("timestamp").as(Long.class), end));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private <T> Predicate[] getPredicatesEqual(CriteriaBuilder cb, Root<EndpointHit> eHit,
                                               Class <T> valueType, Set<T> set, String field) {
        List<Predicate> predicates = new ArrayList<>();
        if (!set.isEmpty()) {
            for (T value : set) {
                predicates.add(cb.equal(eHit.get(field).as(String.class), value));
            }
        } else {
            predicates.add(cb.conjunction());
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
