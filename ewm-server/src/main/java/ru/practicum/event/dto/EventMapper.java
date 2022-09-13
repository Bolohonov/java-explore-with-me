package ru.practicum.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.Compilation;
import ru.practicum.event.Event;
import ru.practicum.event.State;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
                event.getDescription(),
                event.getEventDate(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration()
        );
    }

    public Event fromEventShortDto(EventShortDto event, Long confirmedRequests, LocalDateTime createdOn,
                                   Long initiatorId, LocalDateTime publishedOn, State state, Long views,
                                   Double locLat, Double locLon, Set<Compilation> comps) {
        return new Event(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory().getId(),
                confirmedRequests,
                createdOn,
                event.getDescription(),
                event.getEventDate(),
                initiatorId,
                event.getPaid(),
                event.getParticipantLimit(),
                publishedOn,
                event.getRequestModeration(),
                state,
                views,
                locLat,
                locLon,
                comps
        );
    }

    public Collection<EventShortDto> toEventShortDto(Collection<Event> events) {
        return events.stream()
                .map(e -> toEventShortDto(e))
                .collect(Collectors.toList());
    }
}
