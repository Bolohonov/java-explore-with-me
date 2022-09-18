package ru.practicum.services.event;

import ru.practicum.model.event.Event;

import java.util.List;
import java.util.Set;

public interface EventService {
    /**
     * Получить событие из репозитория
     */
    Event getEventFromRepository(Long eventId);

    /**
     * Получить Set с параметрами
     */
    <T> Set<T> getSetOfParams(List<T> list);
}
