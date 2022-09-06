package ru.practicum.request;

import ru.practicum.request.dto.RequestDto;

import java.util.Collection;
import java.util.Optional;

public interface RequestService {
    Collection<RequestDto> getUserRequests(Long userId);
    Optional<RequestDto> addNewRequest(Long userId, Long eventId);

}
