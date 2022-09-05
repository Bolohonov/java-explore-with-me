package ru.practicum.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final EventService eventService;

    public static EventFullDto toEventFullDto(Event event, String initiator, String category) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new EventFullDto.CategoryDto(event.getCategoryId(), category),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                new EventFullDto.UserShortDto(event.getInitiatorId(), initiator),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getViews()
        );
    }
}
