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
        Root<Like> like = query.from(Like.class);
        CriteriaQuery<Long> likes = query.select(cb.count(like))
                .where(cb.and(cb.equal(like.get("eventId").as(Long.class), eventId),
                        cb.equal(like.get("reason").as(Boolean.class), Boolean.TRUE)));
        CriteriaQuery<Long> dislikes = query.select(cb.count(like))
                .where(cb.and(cb.equal(like.get("eventId").as(Long.class), eventId),
                        cb.equal(like.get("reason").as(Boolean.class), Boolean.FALSE)));
        Long l = entityManager.createQuery(likes).getSingleResult();
        Long d = entityManager.createQuery(dislikes).getSingleResult();
        Long rating = 0L;
        if (l != null && d != null) {
            rating = l - d;
        }
        if (l != null && d == null) {
            rating = l;
        }
        if (l == null && d != null) {
            rating = -d;
        }
        return rating;
    }
}
