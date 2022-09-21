package ru.practicum.repository.like;

import ru.practicum.model.like.Like;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class LikeCustomRepositoryImpl implements LikeCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long countEventRating(Long eventId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Like> event = query.from(Like.class);
        CriteriaQuery<Long> likes = query.select(cb.count(event.get("userId")))
                .where(cb.and(cb.equal(event.get("eventId").as(Long.class), eventId),
                        cb.equal(event.get("reason").as(Boolean.class), Boolean.TRUE)));
        CriteriaQuery<Long> dislikes = query.select(cb.count(event.get("userId")))
                .where(cb.and(cb.equal(event.get("eventId").as(Long.class), eventId),
                        cb.equal(event.get("reason").as(Boolean.class), Boolean.FALSE)));
        return entityManager.createQuery(likes).getSingleResult()
                - entityManager.createQuery(dislikes).getSingleResult();
    }
}
