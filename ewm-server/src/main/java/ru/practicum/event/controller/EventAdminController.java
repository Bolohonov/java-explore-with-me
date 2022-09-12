package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(OK)
    public Collection<EventFullDto> findEventsByUser(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<String> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10")
                                                         Integer size) {
        return eventService.findEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventShortDto> updateEvent(@PathVariable Long eventId,
                                               @RequestBody @Valid Event newEvent) {
        return eventService.updateEventByAdmin(eventId, newEvent);

    }

    @PatchMapping("/{eventId}/publish")
    @ResponseStatus(OK)
    public Optional<EventFullDto> publishEvent(@PathVariable Long eventId,
                                               @RequestBody @Valid Event newEvent) {
        return eventService.publishEventByAdmin(eventId, newEvent);

    }

    @PatchMapping("/{eventId}/reject")
    @ResponseStatus(OK)
    public Optional<EventFullDto> rejectEvent(@PathVariable Long eventId,
                                               @RequestBody @Valid Event newEvent) {
        return eventService.rejectEventByAdmin(eventId, newEvent);

    }
}
