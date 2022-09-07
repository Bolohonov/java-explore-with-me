package ru.practicum.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.CategoryService;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.user.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final UserService userService;
    private final CategoryService categoryService;

    public EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new EventFullDto.CategoryDto(event.getCategoryId(),
                        categoryService.getCategoryById(event.getCategoryId()).get().getName()),
                event.getConfirmedRequests(),
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
        Collection<EventFullDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(toEventFullDto(event));
        }
        return dtos;
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new EventShortDto.CategoryDto(event.getCategoryId(),
                        categoryService.getCategoryById(event.getCategoryId()).get().getName()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                new EventShortDto.UserShortDto(event.getInitiatorId(),
                        userService.getUserById(event.getInitiatorId()).get().getName()),
                event.getPaid(),
                event.getViews()
        );
    }

    public Collection<EventShortDto> toEventShortDto(Collection<Event> events) {
        Collection<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(toEventShortDto(event));
        }
        return dtos;
    }
}
