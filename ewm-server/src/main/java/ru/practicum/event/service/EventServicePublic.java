package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventServicePublic {
    Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable, String sort,
                                        Integer from, Integer size);

    Optional<EventFullDto> getPublishedEventById(Long eventId);
}
