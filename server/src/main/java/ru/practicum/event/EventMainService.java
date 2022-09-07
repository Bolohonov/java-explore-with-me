package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.Validation;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventMainService implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Validation validation;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> getEvents(Map<String, String> allParams) {
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EventFullDto> getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> findEventsByUser(Long userId) {
        return eventMapper.toEventShortDto(eventRepository.findEventsByInitiatorId(userId));
    }

    @Override
    public Optional<EventShortDto> updateEventByInitiator(Long userId, EventShortDto event) {
        if (!getEventById(event.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        validation.validateEventDate(event);
        if (!getEventById(event.getId()).get().getInitiator().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (getEventById(event.getId()).get().getState().equals(State.PENDING)
                || getEventById(event.getId()).get().getState().equals(State.CANCELED)) {
            return null; //TODO add return
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Optional<EventFullDto> addEvent(Long userId, Event event) {
        validation.validateUserActivation(userId);
        validation.validateEventDate(event);
        return of(eventMapper.toEventFullDto(eventRepository.save(event)));
    }
}
