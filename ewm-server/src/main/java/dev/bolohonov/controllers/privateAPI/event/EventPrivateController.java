package dev.bolohonov.controllers.privateAPI.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import dev.bolohonov.model.event.dto.EventAddDto;
import dev.bolohonov.model.event.dto.EventFullDto;
import dev.bolohonov.model.event.dto.EventShortDto;
import dev.bolohonov.model.event.dto.EventUpdateDto;
import dev.bolohonov.model.user.dto.UserWithRatingDto;
import dev.bolohonov.services.event.EventServicePrivate;
import dev.bolohonov.services.request.RequestService;
import dev.bolohonov.model.request.dto.RequestDto;

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
    private final EventServicePrivate eventService;
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
    public Optional<EventFullDto> patchEventByInitiator(@PathVariable Long userId,
                                                        @RequestBody EventUpdateDto event) {
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
    public Optional<EventFullDto> changeEventStateToCanceled(@PathVariable Long userId,
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
                                               @PathVariable Long eventId,
                                               @PathVariable Long reqId) {
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    @ResponseStatus(OK)
    public Optional<RequestDto> rejectRequest(@PathVariable Long userId,
                                              @PathVariable Long reqId) {
        return requestService.rejectRequest(userId, reqId);
    }

    @PostMapping("/{eventId}/like")
    @ResponseStatus(OK)
    public Optional<EventFullDto> likeEvent(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.debug("Получен запрос в контроллер на добавление лайка события с id {}", eventId);
        return eventService.addLike(userId, eventId);
    }

    @PostMapping("/{eventId}/dislike")
    @ResponseStatus(OK)
    public Optional<EventFullDto> dislikeEvent(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.debug("Получен запрос в контроллер на добавление дизлайка события с id {}", eventId);
        return eventService.addDislike(userId, eventId);
    }

    @GetMapping("/rating")
    @ResponseStatus(OK)
    public Collection<UserWithRatingDto> getUsersByEventsRating(@PathVariable Long userId,
                                                                @PositiveOrZero @RequestParam(defaultValue = "0")
                                                                    Integer userFrom,
                                                                @Positive @RequestParam(defaultValue = "10")
                                                                    Integer size) {
        log.debug("Получен запрос в контроллер на получение рейтинга пользователей");
        return eventService.getUsersByRating(userFrom, size);
    }
}
