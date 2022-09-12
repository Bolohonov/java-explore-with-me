package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.RequestDto;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    @ResponseStatus(OK)
    public Collection<RequestDto> findUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/{userId}/requests/{eventId}")
    @ResponseStatus(OK)
    public Optional<RequestDto> addRequest(@PathVariable Long userId,
                                           @PathVariable Long eventId) {
        return requestService.addNewRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(OK)
    public Optional<RequestDto> cancelRequest(@PathVariable Long userId,
                                              @PathVariable Long requestId) {
        return requestService.revokeRequest(userId, requestId);
    }
}
