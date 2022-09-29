package ru.practicum.repository.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.feedback.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackCountRepository {
    Feedback findByUserIdAndEventId(Long userId, Long eventId);
}
