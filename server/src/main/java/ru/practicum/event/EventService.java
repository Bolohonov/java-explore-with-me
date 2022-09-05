package ru.practicum.event;

import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;
import java.util.Map;

public interface EventService {
    Collection<EventShortDto> getEvents(Map<String, String> allParams);

    EventShortDto getEventById(Long eventId);
}
