package ru.practicum.event.repository;

import ru.practicum.event.Event;
import ru.practicum.event.State;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface EventRepositoryCustom {
    Collection<Event> getEventsByStates(Set<Long> ids,
                                        Set<String> states,
                                        Set<Long> setOfCategories,
                                        Map<String, LocalDateTime> timeMap);
}
