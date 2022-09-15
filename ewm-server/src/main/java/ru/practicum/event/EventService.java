package ru.practicum.event;

import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventService {
    Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable, String sort,
                                        Integer from, Integer size);

    Optional<EventFullDto> getEventById(Long eventId);

    Optional<EventFullDto> getPublishedEventById(Long eventId);

    Collection<EventShortDto> findEventsByInitiator(Long userId, Integer from, Integer size);

    Optional<EventShortDto> updateEventByInitiator(Long userId, EventShortDto event);

    Optional<EventFullDto> addEvent(Long userId, EventAddDto event);

    Optional<EventShortDto> changeEventStateToCanceled(Long userId, Long eventId);

    Collection<EventFullDto> findEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size);

    Optional<EventShortDto> updateEventByAdmin(Long eventId, Event newEvent);

    Optional<EventFullDto> publishEventByAdmin(Long eventId, Event newEvent);

    Optional<EventFullDto> rejectEventByAdmin(Long eventId, Event newEvent);
}
