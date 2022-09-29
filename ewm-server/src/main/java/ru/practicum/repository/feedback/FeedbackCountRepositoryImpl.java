package ru.practicum.repository.feedback;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.feedback.Feedback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class FeedbackCountRepositoryImpl implements FeedbackCountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long countEventRating(Long eventId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Feedback> like = query.from(Feedback.class);
        CriteriaQuery<Object[]> likes = query.multiselect(like.get("isLike").alias("isLike"),
                        cb.count(like.get("isLike")).alias("count"))
                .where(cb.equal(like.get("eventId"), eventId))
                .groupBy(like.get("isLike"));
        Long result = 0L;
        for (Object[] arr : entityManager.createQuery(likes).getResultList()) {
            if (arr[0].equals(true)) {
                result =  result + (Long) arr[1];
            } else {
                result =  result - (Long) arr[1];
            }
        }
        return result;
    }
}
