package ru.practicum.model.event.mappers.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.model.event.dto.EventAddDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.EventUpdateDto;
import ru.practicum.repository.category.CategoryRepository;
import ru.practicum.errors.ApiError;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.services.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventMapper {
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    public EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new EventFullDto.CategoryDto(event.getCategory(),
                        categoryRepository.findById(event.getCategory()).get().getName()),
                event.getConfirmedRequests() != null ? event.getConfirmedRequests() : null,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                new EventFullDto.UserShortDto(event.getInitiatorId(),
                        userService.getUserById(event.getInitiatorId()).get().getName()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getViews(),
                new EventFullDto.Location(event.getLocLat(), event.getLocLon())
        );
    }

    public Collection<EventFullDto> toEventFullDto(Collection<Event> events) {
        return events.stream()
                .map(e -> toEventFullDto(e))
                .collect(Collectors.toList());
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new EventShortDto.CategoryDto(event.getCategory(),
                        categoryRepository.findById(event.getCategory()).get().getName()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                new EventShortDto.UserShortDto(event.getInitiatorId(),
                        userService.getUserById(event.getInitiatorId()).get().getName()),
                event.getPaid(),
                event.getViews()
        );
    }

    public Event fromEventShortDto(EventShortDto event, String description, LocalDateTime createdOn,
                                   Integer participantLimit, Boolean requestModeration, LocalDateTime publishedOn,
                                   State state, Double locLat, Double locLon) {
        return new Event(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory().getId(),
                event.getConfirmedRequests(),
                createdOn,
                description,
                event.getEventDate(),
                event.getInitiator().getId(),
                event.getPaid(),
                participantLimit,
                publishedOn,
                requestModeration,
                state,
                event.getViews(),
                locLat,
                locLon
        );
    }

    public Collection<EventShortDto> toEventShortDto(Collection<Event> events) {
        return events.stream()
                .map(e -> toEventShortDto(e))
                .collect(Collectors.toList());
    }

    public Event fromEventAddDto(EventAddDto event, Long userId) throws ApiError {
        log.info("Получено событие для конвертации");
        if (event.getLocation() == null) {
            throw new ApiError(HttpStatus.BAD_REQUEST,
                    "Ошибка создания события", "Вы не указали локацию");
        }
        return new Event(
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                0L,
                LocalDateTime.now(),
                event.getDescription(),
                event.getEventDate(),
                userId,
                event.getPaid(),
                event.getParticipantLimit() != null ? event.getParticipantLimit() : 0,
                null,
                event.getRequestModeration(),
                State.PENDING,
                0L,
                event.getLocation().getLat(),
                event.getLocation().getLon()
        );
    }

    public Event fromEventAddDtoToUpdate(EventAddDto newEvent, Event oldEvent, Long confirmedRequests,
                                         LocalDateTime createdOn, Long initiatorId,
                                         LocalDateTime publishedOn, State state, Long views) throws ApiError {
        log.info("Получено событие для конвертации");
        return new Event(
                newEvent.getTitle() != null ? newEvent.getTitle() : oldEvent.getTitle(),
                newEvent.getAnnotation() != null ? newEvent.getAnnotation() : oldEvent.getAnnotation(),
                newEvent.getCategory() != null ? newEvent.getCategory() : oldEvent.getCategory(),
                confirmedRequests,
                createdOn,
                newEvent.getDescription() != null ? newEvent.getDescription() : oldEvent.getDescription(),
                newEvent.getEventDate() != null ? newEvent.getEventDate() : oldEvent.getEventDate(),
                initiatorId,
                newEvent.getPaid() != null ? newEvent.getPaid() : oldEvent.getPaid(),
                newEvent.getParticipantLimit() != null ? newEvent.getParticipantLimit()
                        : oldEvent.getParticipantLimit(),
                publishedOn,
                newEvent.getRequestModeration() != null ? newEvent.getRequestModeration()
                        : oldEvent.getRequestModeration(),
                state,
                views,
                newEvent.getLocation() != null ? newEvent.getLocation().getLat() : oldEvent.getLocLat(),
                newEvent.getLocation() != null ? newEvent.getLocation().getLon() : oldEvent.getLocLon()
        );
    }

    public Event fromEventAddDtoToUpdate(EventUpdateDto newEvent, Event oldEvent, Long confirmedRequests,
                                         LocalDateTime createdOn, Long initiatorId,
                                         LocalDateTime publishedOn, State state, Long views) throws ApiError {
        log.info("Получено событие для конвертации");
        return new Event(
                oldEvent.getId(),
                newEvent.getTitle() != null ? newEvent.getTitle() : oldEvent.getTitle(),
                newEvent.getAnnotation() != null ? newEvent.getAnnotation() : oldEvent.getAnnotation(),
                newEvent.getCategory() != null ? newEvent.getCategory() : oldEvent.getCategory(),
                confirmedRequests,
                createdOn,
                newEvent.getDescription() != null ? newEvent.getDescription() : oldEvent.getDescription(),
                newEvent.getEventDate() != null ? newEvent.getEventDate() : oldEvent.getEventDate(),
                initiatorId,
                newEvent.getPaid() != null ? newEvent.getPaid() : oldEvent.getPaid(),
                newEvent.getParticipantLimit() != null ? newEvent.getParticipantLimit()
                        : oldEvent.getParticipantLimit(),
                publishedOn,
                newEvent.getRequestModeration() != null ? newEvent.getRequestModeration()
                        : oldEvent.getRequestModeration(),
                state,
                views,
                newEvent.getLocation() != null ? newEvent.getLocation().getLat() : oldEvent.getLocLat(),
                newEvent.getLocation() != null ? newEvent.getLocation().getLon() : oldEvent.getLocLon()
        );
    }

    public EventAddDto toEventAddDto(Event event) throws ApiError {
        log.info("Получено событие для конвертации");
        return new EventAddDto(
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.getDescription(),
                event.getEventDate(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                new EventAddDto.Location(event.getLocLat(), event.getLocLon())
        );
    }
}
