package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.error.ApiError;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.UserService;
import ru.practicum.user.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventMainService implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, String sort,
                                               Integer from, Integer size) {
        return eventMapper.toEventShortDto(eventRepository.getEvents(text,
                getSetAndValidateParams(Long.class, categories), paid,
                getAndValidateTimeRangeWithDefault(rangeStart, rangeEnd),
                onlyAvailable,
                getSortString(sort), from, size));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EventFullDto> getPublishedEventById(Long eventId) {
        Event event = getEventFromRepository(eventId);
        if (event.getPublishedOn() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return of(eventMapper.toEventFullDto(event));
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
        setDefaultFields(userId, event);
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
    public Collection<EventFullDto> findEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        Collection<Event> events = eventRepository.getEventsByAdmin(
                getSetAndValidateParams(Long.class, users),
                getSetAndValidateParams(String.class, states),
                getSetAndValidateParams(Long.class, categories),
                getAndValidateTimeRange(rangeStart, rangeEnd),
                from,
                size
                );
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
            throw new ApiError(HttpStatus.BAD_REQUEST, "Field: " +
                    "eventDate. Error: must be a date in the present or in the future. Value: %s",
                    event.getEventDate().toString());
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
                .getActivation().equals(Boolean.TRUE)) {
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

    private void setDefaultFields(Long userId, Event event) {
        event.setInitiatorId(userId);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
    }

    private <T> Set<T> getSetAndValidateParams(Class<T> type, List<T> list) {
        Set<T> set = new HashSet<>();
        if (list != null) {
            set.addAll(list);
        }
        return set;
    }

    private Map<String, LocalDateTime> getAndValidateTimeRange(String rangeStart, String rangeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz",
                Locale.getDefault());
        Map<String, LocalDateTime> timeMap = new HashMap<>();
        if (rangeStart != null) {
            LocalDateTime parsedStart = LocalDateTime.parse(rangeStart, formatter);
            timeMap.put("start", parsedStart);
        }
        if (rangeEnd != null) {
            LocalDateTime parsedEnd = LocalDateTime.parse(rangeEnd, formatter);
            timeMap.put("end", parsedEnd);
        }
        return timeMap;
    }

    private Map<String, LocalDateTime> getAndValidateTimeRangeWithDefault(String rangeStart, String rangeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz",
                Locale.getDefault());
        Map<String, LocalDateTime> timeMap = new HashMap<>();
        if (rangeStart != null) {
            LocalDateTime parsedStart = LocalDateTime.parse(rangeStart, formatter);
            timeMap.put("start", parsedStart);
        } else {
            timeMap.put("start", LocalDateTime.now());
        }
        if (rangeEnd != null) {
            LocalDateTime parsedEnd = LocalDateTime.parse(rangeEnd, formatter);
            timeMap.put("end", parsedEnd);
        }
        return timeMap;
    }

    private String getSortString(String sort) {
        String result = "id";
        if (sort != null && sort.equals("EVENT_DATE")) {
            result = "eventDate";

        }
        if (sort != null && sort.equals("VIEWS")) {
            result = "views";

        }
        return result;
    }
}
