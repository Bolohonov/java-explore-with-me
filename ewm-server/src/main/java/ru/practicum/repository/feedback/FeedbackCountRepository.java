package ru.practicum.repository.feedback;

public interface FeedbackCountRepository {
    /**
     * Рассчитать рейтинг события по его id
     */
    Long countEventRating(Long eventId);
}
