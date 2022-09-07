package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.Request;

import java.util.Collection;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> getRequestsByRequester(Long requesterId);
    Optional<Request> getRequestByRequesterAndEvent(Long requesterId, Long eventId);
}
