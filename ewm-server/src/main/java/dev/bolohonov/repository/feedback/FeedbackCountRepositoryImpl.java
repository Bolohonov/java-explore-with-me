package dev.bolohonov.repository.feedback;

import org.springframework.transaction.annotation.Transactional;
import dev.bolohonov.model.feedback.Feedback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class FeedbackCountRepositoryImpl implements FeedbackCountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long getRating(Long eventId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Feedback> like = query.from(Feedback.class);
        CriteriaQuery<Object[]> likes = query.multiselect(
                like.get("isLike"), cb.count(like.get("isLike")))
                .where(cb.equal(like.get("eventId"), eventId))
                .groupBy(like.get("isLike"));
        Long result = 0L;
        for (Object[] arr : entityManager.createQuery(likes).getResultList()) {
            result = arr[0].equals(true) ? result + (Long) arr[1] : result - (Long) arr[1];
        }
        return result;
    }
}
