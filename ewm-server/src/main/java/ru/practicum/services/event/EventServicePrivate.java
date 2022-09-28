package ru.practicum.services.event;

import ru.practicum.model.event.dto.EventAddDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.EventUpdateDto;
import ru.practicum.model.user.dto.UserWithRatingDto;

import java.util.Collection;
import java.util.Optional;

public interface EventServicePrivate {
    /**
     * Получить событие по id
     */
    Optional<EventFullDto> getEventById(Long eventId);

    /**
     * Получить список событий по id инициатора
     */
    Collection<EventShortDto> findEventsByInitiator(Long userId, Integer from, Integer size);

    /**
     * Инициатор обновляет событие
     */
    Optional<EventFullDto> updateEventByInitiator(Long userId, EventUpdateDto event);

    /**
     * Добавить событие
     */
    Optional<EventFullDto> addEvent(Long userId, EventAddDto event);

    /**
     * Отменить событие - статус Canceled
     */
    Optional<EventFullDto> changeEventStateToCanceled(Long userId, Long eventId);

    /**
     * Лайкнуть/дизлайкнуть событие
     */
    Optional<EventFullDto> addLikeOrDislike(Long userId, Long eventId, Boolean like);

    /**
     * Получение списка инициаторов событий по их рейтингу
     */
    Collection<UserWithRatingDto> getUsersByRating(Integer from, Integer size);
}
