package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(OK)
    public Collection<EventShortDto> findEvents(@RequestParam Map<String,String> allParams) {
        return eventService.getEvents(allParams);
    }
}
