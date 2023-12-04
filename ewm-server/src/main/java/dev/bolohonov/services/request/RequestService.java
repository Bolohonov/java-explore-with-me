package dev.bolohonov.services.request;

import dev.bolohonov.model.request.dto.RequestDto;

import java.util.Collection;
import java.util.Optional;

public interface RequestService {
    /**
     * Получить запросы пользователя
     */
    Collection<RequestDto> getUserRequests(Long userId);

    /**
     * Добавить запрос
     */
    Optional<RequestDto> addNewRequest(Long userId, Long eventId);

    /**
     * Поменять статус запроса на Rejected
     */
    Optional<RequestDto> revokeRequest(Long userId, Long requestId);

    /**
     * Получить список запросов к событию
     */
    Collection<RequestDto> getRequestsOfEventInitiator(Long initiatorId, Long eventId);

    /**
     * Подтвердить запрос - статус CONFIRMED
     */
    Optional<RequestDto> confirmRequest(Long userId, Long eventId, Long requestId);

    /**
     * Отклонить запрос - статус REJECTED
     */
    Optional<RequestDto> rejectRequest(Long userId, Long requestId);
}
