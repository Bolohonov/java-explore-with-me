package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.Compilation;
import ru.practicum.event.Event;

import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    Page<Event> findEventsByInitiatorId(Long initiatorId, Pageable pageable);
}
