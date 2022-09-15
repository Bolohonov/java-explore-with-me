package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.RequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping()
    @ResponseStatus(OK)
    public Collection<EventShortDto> findEventsByUser(@PathVariable Long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                      Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10")
                                                      Integer size) {
        return eventService.findEventsByInitiator(userId, from, size);
    }

    @PatchMapping()
    @ResponseStatus(OK)
    public Optional<EventShortDto> patchEventByInitiator(@PathVariable Long userId,
                                                         @RequestBody EventShortDto event) {
        return eventService.updateEventByInitiator(userId, event);
    }

    @PostMapping()
    @ResponseStatus(OK)
    public Optional<EventFullDto> addNewEvent(@PathVariable Long userId,
                                              @RequestBody EventAddDto event) {
        return eventService.addEvent(userId, event);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventFullDto> findEventById(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        return eventService.getEventById(eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventShortDto> changeEventStateToCanceled(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return eventService.changeEventStateToCanceled(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(OK)
    public Collection<RequestDto> findRequestsOfEventInitiator(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {
        return requestService.getRequestsOfEventInitiator(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    @ResponseStatus(OK)
    public Optional<RequestDto> confirmRequest(@PathVariable Long userId,
                                               @PathVariable Long reqId) {
        return requestService.confirmRequest(userId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    @ResponseStatus(OK)
    public Optional<RequestDto> rejectRequest(@PathVariable Long userId,
                                              @PathVariable Long reqId) {
        return requestService.rejectRequest(userId, reqId);
    }
}
