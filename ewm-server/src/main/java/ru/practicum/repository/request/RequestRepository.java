package ru.practicum.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.request.Request;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    /**
     * Получить список запросов по id пользователя, создавшего запрос requesterId
     */
    Collection<Request> getRequestsByRequester(Long requesterId);

    /**
     * Получить список запросов по id события к которому создан запрос eventId
     */
    Collection<Request> getRequestsByEvent(Long eventId);

    /**
     * Получить список запросов по id пользователя requesterId и id события eventId
     */
    Request getRequestByRequesterAndEvent(Long requesterId, Long EventId);

    /**
     * Получить список запросов по id события eventId и статусу status
     */
    @Query("select r from Request as r  " +
            "where (r.event = ?1 and r.status = ?2) order by r.id")
    Collection<Request> getRequestsByEventAndStatus(Long eventId, String status);
}
