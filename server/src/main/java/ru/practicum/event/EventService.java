package ru.practicum.event;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface EventService {
    Collection<EventShortDto> getEvents(Map<String, String> allParams);

    Optional<EventFullDto> getEventById(Long eventId);

    Collection<EventShortDto> findEventsByUser(Long userId);

    Optional<EventShortDto> updateEventByInitiator(Long userId, EventShortDto event);

    Optional<EventFullDto> addEvent(Long userId, Event event);


}
