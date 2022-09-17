package ru.practicum.services.event;

import ru.practicum.model.event.dto.EventAddDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.Collection;
import java.util.Optional;

public interface EventServicePrivate {
    Optional<EventFullDto> getEventById(Long eventId);

    Collection<EventShortDto> findEventsByInitiator(Long userId, Integer from, Integer size);

    Optional<EventFullDto> updateEventByInitiator(Long userId, EventShortDto event);

    Optional<EventFullDto> addEvent(Long userId, EventAddDto event);

    Optional<EventShortDto> changeEventStateToCanceled(Long userId, Long eventId);
}
