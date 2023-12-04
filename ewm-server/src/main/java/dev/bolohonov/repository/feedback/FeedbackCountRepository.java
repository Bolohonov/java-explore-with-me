package dev.bolohonov.repository.feedback;

public interface FeedbackCountRepository {
    /**
     * Рассчитать рейтинг события по его id
     */
    Long getRating(Long eventId);
}
