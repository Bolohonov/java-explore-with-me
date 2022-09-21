package ru.practicum.repository.like;

public interface LikeCustomRepository {
    /**
     * Рассчитать рейцтинг события по его id
     */
    Long countEventRating(Long eventId);
}
