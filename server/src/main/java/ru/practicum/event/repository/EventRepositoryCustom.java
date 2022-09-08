package ru.practicum.event.repository;

import ru.practicum.event.Event;
import ru.practicum.event.State;

import java.util.Collection;
import java.util.Set;

public interface EventRepositoryCustom {
    Collection<Event> getEventsByStates(Set<String> states);
}
