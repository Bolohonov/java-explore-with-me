package ru.practicum.services.request;

import ru.practicum.model.request.dto.RequestDto;

import java.util.Collection;
import java.util.Optional;

public interface RequestService {
    Collection<RequestDto> getUserRequests(Long userId);

    Optional<RequestDto> addNewRequest(Long userId, Long eventId);

    Optional<RequestDto> revokeRequest(Long userId, Long requestId);

    Collection<RequestDto> getRequestsOfEventInitiator(Long initiatorId, Long eventId);

    Optional<RequestDto> confirmRequest(Long userId, Long eventId, Long requestId);

    Optional<RequestDto> rejectRequest(Long userId, Long requestId);
}
