package ru.practicum.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.Event;
import ru.practicum.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
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
                event.getViews()
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

    public Collection<EventShortDto> toEventShortDto(Collection<Event> events) {
        return events.stream()
                .map(e -> toEventShortDto(e))
                .collect(Collectors.toList());
    }
}
