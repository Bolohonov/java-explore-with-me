package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.event.State;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.RequestDto;

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
    public Collection<EventShortDto> findEventsByUser(@PathVariable Long userId) {
        return eventService.findEventsByUser(userId);
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
                                                        @RequestBody Event event) {
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
        return eventService.updateEventByInitiator(userId,
                eventService.getEventById(eventId).get().setState(State.CANCELED);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(OK)
    public Optional<RequestDto> findRequestByUser(@PathVariable Long userId,
                                                  @PathVariable Long eventId) {
        return requestService.getUserRequestOfEvent(userId, eventId);
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
