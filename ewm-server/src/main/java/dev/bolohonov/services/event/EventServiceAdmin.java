package dev.bolohonov.services.event;

import dev.bolohonov.model.event.dto.EventAddDto;
import dev.bolohonov.model.event.dto.EventFullDto;

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
    Optional<EventAddDto> updateEvent(Long eventId, EventAddDto newEvent);

    /**
     * Опубликовать событие
     */
    Optional<EventFullDto> publishEvent(Long eventId);

    /**
     * Отклонить запрос на публикацию события
     */
    Optional<EventFullDto> rejectEvent(Long eventId);
}
