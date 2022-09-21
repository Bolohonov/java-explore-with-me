package ru.practicum.repository.like;

public interface LikeRepositoryCustom {
    /**
     * Рассчитать рейтинг события по его id
     */
    Long countEventRating(Long eventId);
}
