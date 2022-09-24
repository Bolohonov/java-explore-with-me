package ru.practicum.repository.like;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.like.Like;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long countEventRating(Long eventId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Like> like = query.from(Like.class);
        System.out.println(eventId);
        CriteriaQuery<Long> likes = query.select(cb.count(like))
                .where(cb.and(cb.equal(like.get("eventId"), eventId),
                        cb.equal(like.get("reason"), Boolean.TRUE)));
        Long l = entityManager.createQuery(likes).getSingleResult();
        CriteriaQuery<Long> dislikes = query.select(cb.count(like))
                .where(cb.and(cb.equal(like.get("eventId"), eventId),
                        cb.equal(like.get("reason"), Boolean.FALSE)));
        Long d = entityManager.createQuery(dislikes).getSingleResult();
        return l - d;
    }
}
