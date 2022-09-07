package ru.practicum.request;

import ru.practicum.request.dto.RequestDto;

import java.util.Collection;
import java.util.Optional;

public interface RequestService {
    Collection<RequestDto> getUserRequests(Long userId);
    Optional<RequestDto> addNewRequest(Long userId, Long eventId);
    Optional<RequestDto> revokeRequest(Long userId, Long requestId);
    Optional<RequestDto> getUserRequestOfEvent(Long userId, Long eventId);

}
