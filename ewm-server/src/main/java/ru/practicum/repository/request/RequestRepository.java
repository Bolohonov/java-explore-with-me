package ru.practicum.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.request.Request;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> getRequestsByRequester(Long requesterId);

    Collection<Request> getRequestsByEvent(Long eventId);

    Request getRequestByRequesterAndEvent(Long requesterId, Long EventId);

    @Query("select r from Request as r  " +
            "where (r.event = ?1 and r.status = ?2) order by r.id")
    Collection<Request> getRequestsByEventAndStatus(Long eventId, String status);
}
