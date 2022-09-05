package ru.practicum.event;

import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface EventService {
    Collection<EventShortDto> getEvents(Map<String, String> allParams);

    Optional<EventShortDto> getEventById(Long eventId);
}
