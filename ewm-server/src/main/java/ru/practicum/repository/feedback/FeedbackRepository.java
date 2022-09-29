package ru.practicum.repository.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.feedback.Feedback;
import ru.practicum.model.feedback.FeedbackId;

public interface FeedbackRepository extends JpaRepository<Feedback, FeedbackId>, FeedbackCountRepository {
    Feedback findByUserIdAndEventId(Long userId, Long eventId);
}
