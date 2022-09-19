package ru.practicum.services.event;

import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventAddDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventServiceAdmin {
    /**
     * Получить список событий администратором
     * определенных пользователей users,
     * определенных статусов states,
     * из выбранных категорий categories,
     * в заданном временном интервале rangeStart - rangeEnd
     * pagination from & size
     */
    Collection<EventFullDto> findEvents(List<Long> users, List<String> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, Integer from, Integer size);

    /**
     * Обновить событие
     */
    Optional<EventShortDto> updateEvent(Long eventId, EventAddDto newEvent);

    /**
     * Опубликовать событие
     */
    Optional<EventFullDto> publishEvent(Long eventId);

    /**
     * Отклонить запрос на публикацию события
     */
    Optional<EventFullDto> rejectEvent(Long eventId);
}
