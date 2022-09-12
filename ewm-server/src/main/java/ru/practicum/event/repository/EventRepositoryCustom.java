package ru.practicum.event.repository;

import ru.practicum.event.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface EventRepositoryCustom {
    Collection<Event> getEvents(String text,
                                Set<Long> setOfCategories,
                                Boolean paid,
                                Map<String, LocalDateTime> timeMap,
                                Boolean onlyAvailable,
                                String sort,
                                Integer from, Integer size);

    Collection<Event> getEventsByAdmin(Set<Long> ids,
                                       Set<String> states,
                                       Set<Long> setOfCategories,
                                       Map<String, LocalDateTime> timeMap,
                                       Integer fom, Integer size);
}
