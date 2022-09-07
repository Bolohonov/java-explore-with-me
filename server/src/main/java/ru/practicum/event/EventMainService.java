package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.UserService;
import ru.practicum.user.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventMainService implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
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
    public Collection<EventShortDto> findEventsByInitiator(Long userId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size,
                Sort.by("id").ascending());
        Iterable<Event> eventsPage = eventRepository.findEventsByInitiatorId(userId, pageRequest);
        Collection<Event> events = new ArrayList<>();
        eventsPage.forEach(events::add);
        return eventMapper.toEventShortDto(events);
    }

    @Override
    public Optional<EventShortDto> updateEventByInitiator(Long userId, EventShortDto event) {
        if (!getEventById(event.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        validateEventDate(event);
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
        validateUserActivation(userId);
        validateEventDate(event);
        return of(eventMapper.toEventFullDto(eventRepository.save(event)));
    }

    @Override
    public Optional<EventShortDto> changeEventStateToCanceled(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
        if (!event.getState().equals(State.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        event.setState(State.CANCELED);
        return of(eventMapper.toEventShortDto(event));
    }

    @Override
    public Collection<EventFullDto> findEventsByAdmin(String[] states, Integer[] categoriesId,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        Collection<Event> events = eventRepository.findEventsByState(states);

        return eventMapper.toEventFullDto(events);
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }

    public void validateEventDate(EventShortDto event) {
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateEventDate(Event event) {
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateUserActivation(Long userId) {
        if (!userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"))
                .isActivation()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
