package ru.practicum.controllers.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventAddDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.services.event.EventServiceAdmin;

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
    private final EventServiceAdmin eventService;

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
        return eventService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventShortDto> updateEvent(@PathVariable Long eventId,
                                               @RequestBody EventAddDto event) {
        log.info("Поступил запрос на обновление события администратором");
        System.out.println(event); //TODO
        return eventService.updateEvent(eventId, event);
    }

    @PatchMapping("/{eventId}/publish")
    @ResponseStatus(OK)
    public Optional<EventFullDto> publishEvent(@PathVariable Long eventId) {
        log.info("Поступил запрос на публикацию события");
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    @ResponseStatus(OK)
    public Optional<EventFullDto> rejectEvent(@PathVariable Long eventId) {
        log.info("Поступил запрос на отклонение публикации события");
        return eventService.rejectEvent(eventId);
    }
}
