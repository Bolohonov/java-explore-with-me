package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.Event;

import java.util.Collection;

public interface EventRepository extends JpaRepository<Event, Long> {
    Collection<Event> findEventsByInitiatorId(Long initiatorId);
}
