package dev.bolohonov.repository.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.bolohonov.model.feedback.Feedback;
import dev.bolohonov.model.feedback.FeedbackId;

public interface FeedbackRepository extends JpaRepository<Feedback, FeedbackId>, FeedbackCountRepository {
    Feedback findByUserIdAndEventId(Long userId, Long eventId);
}
