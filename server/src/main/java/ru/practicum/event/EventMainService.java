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
        Event event = getEventFromRepository(eventId);
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
        Event event = getEventFromRepository(eventId);
        if (!event.getState().equals(State.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        event.setState(State.CANCELED);
        return of(eventMapper.toEventShortDto(event));
    }

    @Override
    public Collection<EventFullDto> findEventsByAdmin(String[] states, Long[] categoriesId,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        Collection<Event> events = eventRepository.findEventsByState(states);

        return eventMapper.toEventFullDto(events);
    }

    @Override
    public Optional<EventShortDto> updateEventByAdmin(Long eventId, Event newEvent) {
        getEventFromRepository(eventId);
        newEvent.setId(eventId);
        return of(eventMapper.toEventShortDto(eventRepository.save(newEvent)));
    }

    @Override
    public Optional<EventFullDto> publishEventByAdmin(Long eventId, Event newEvent) {
        validateEventForPublishing(eventId, newEvent);
        return of(eventMapper.toEventFullDto(eventRepository.save(newEvent)));
    }

    @Override
    public Optional<EventFullDto> rejectEventByAdmin(Long eventId, Event newEvent) {
        validateEventForRejecting(eventId);
        return of(eventMapper.toEventFullDto(eventRepository.save(newEvent)));
    }

    private Event getEventFromRepository(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }

    private void validateEventDate(EventShortDto event) {
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEventDate(Event event) {
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void validateUserActivation(Long userId) {
        if (!userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"))
                .isActivation()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEventForPublishing(Long eventId, Event newEvent) {
        Event event = getEventFromRepository(eventId);
        if (newEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEventForRejecting(Long eventId) {
        Event event = getEventFromRepository(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


}
