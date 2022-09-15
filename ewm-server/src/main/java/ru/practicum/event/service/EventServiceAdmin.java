package ru.practicum.event.service;

import ru.practicum.event.Event;
import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventServiceAdmin {
    Collection<EventFullDto> findEvents(List<Long> users, List<String> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, Integer from, Integer size);

    Optional<EventShortDto> updateEvent(Long eventId, Event newEvent);

    Optional<EventFullDto> publishEvent(Long eventId);

    Optional<EventFullDto> rejectEvent(Long eventId);
}
