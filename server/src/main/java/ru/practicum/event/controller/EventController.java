package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(OK)
    public Collection<EventShortDto> findEvents(@RequestParam(name = "text", required = false) String text,
                                             @RequestParam(name = "categories", required = false)
                                             List<Long> categories,
                                             @RequestParam(name = "paid", required = false) Boolean paid,
                                             @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                             @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                             Boolean onlyAvailable,
                                             @RequestParam(name = "sort", required = false)
                                             String sort,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                             Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10")
                                             Integer size) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventFullDto> findEventById(@PathVariable Long eventId) {
        return eventService.getPublishedEventById(eventId);
    }
}
