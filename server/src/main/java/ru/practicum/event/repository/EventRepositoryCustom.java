package ru.practicum.event.repository;

import ru.practicum.event.Event;

import java.util.Collection;

public interface EventRepositoryCustom {
    Collection<Event> findEventsByState(String[] state);

//    , Integer[] categoriesId,
//    String rangeStart, String rangeEnd
}
