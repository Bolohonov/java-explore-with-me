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
import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.UserService;

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
    private static final String ER_OBJ = "событие";

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, String sort,
                                               Integer from, Integer size) {
        log.info("Получен запрос на вывод списка событий");
        Collection<Event> events = eventRepository.getEvents(text,
                getSetAndValidateParams(Long.class, categories), paid,
                getAndValidateTimeRangeWithDefault(rangeStart, rangeEnd),
                onlyAvailable,
                getSortString(sort), from, size);
        events.stream().forEach(e -> e.addView());
        return eventMapper.toEventShortDto(events);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EventFullDto> getPublishedEventById(Long eventId) {
        log.info("Получен запрос на вывод списка опубликованных событий");
        Event event = getEventFromRepository(eventId);
        if (event.getPublishedOn() == null) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Событие не найдено",
                    String.format("При выполнении %s не найден %s c id %s",
                            "getPublishedEventById", ER_OBJ, eventId));
        }
        event.addView();
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EventFullDto> getEventById(Long eventId) {
        log.info("Получен запрос на поиск события");
        Event event = getEventFromRepository(eventId);
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> findEventsByInitiator(Long userId, Integer from, Integer size) {
        log.info("Получен запрос в сервис на поиск события по инициатору");
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size,
                Sort.by("id").ascending());
        Iterable<Event> eventsPage = eventRepository.findEventsByInitiatorId(userId, pageRequest);
        Collection<Event> events = new ArrayList<>();
        eventsPage.forEach(events::add);
        return eventMapper.toEventShortDto(events);
    }

    @Transactional
    @Override
    public Optional<EventShortDto> updateEventByInitiator(Long userId, EventShortDto event) {
        log.info("Получен запрос в сервис на обновление события инициатором");
        Event oldEvent = getEventFromRepository(event.getId());
        List<ApiError> errorsList = ApiError.getErrorsList();
        try {
            validateEventDate(event);
        } catch (ApiError error) {
            errorsList.add(error);
        }
        if (!getEventById(event.getId()).get().getInitiator().getId().equals(userId)) {
            errorsList.add(new ApiError(HttpStatus.BAD_REQUEST, String.format("При выполнении %s произошла ошибка.",
                    "update"), String.format("Пользователь с id %s не является инициатором события. Отказано в доступе",
                            userId)));
        }
        if (!getEventById(event.getId()).get().getState().equals(State.PENDING)
                || !getEventById(event.getId()).get().getState().equals(State.CANCELED)) {
            errorsList.add(new ApiError(HttpStatus.BAD_REQUEST, String.format("При выполнении %s произошла ошибка.",
                    "update"), String.format("Событие с id %s уже имеет статус Опубликовано. Отказано в доступе",
                    event.getId())));
        }
        if (!errorsList.isEmpty()) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при обновлении события",
                    String.format("Во время обновления события %s произошли ошибки:",
                            event.getId()), errorsList);
        }
        return of(updateEventInRepository(oldEvent, event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> addEvent(Long userId, EventAddDto event) {
        log.info("Получен запрос в сервис на добавление события");
        validateUserActivation(userId);
        validateEventDate(event);
        setNewEventState(event);
        Event newEvent = eventMapper.fromEventAddDto(event);
        setDefaultFields(userId, newEvent);
        return of(eventMapper.toEventFullDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventShortDto> changeEventStateToCanceled(Long userId, Long eventId) {
        log.info("Получен запрос на отмену события");
        Event event = getEventFromRepository(eventId);
        if (!event.getState().equals(State.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        event.setState(State.CANCELED);
        return of(eventMapper.toEventShortDto(event));
    }

    @Transactional
    @Override
    public Collection<EventFullDto> findEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("Получен запрос на вывод списка событий администратором");
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

    @Transactional
    @Override
    public Optional<EventShortDto> updateEventByAdmin(Long eventId, Event newEvent) {
        log.info("Получен запрос на обновление списка событий администратором");
        getEventFromRepository(eventId);
        newEvent.setId(eventId);
        return of(eventMapper.toEventShortDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> publishEventByAdmin(Long eventId, Event newEvent) {
        log.info("Получен запрос на публикацию события администратором");
        validateEventForPublishing(eventId, newEvent);
        return of(eventMapper.toEventFullDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> rejectEventByAdmin(Long eventId, Event newEvent) {
        log.info("Получен запрос на отмену события администратором");
        validateEventForRejecting(eventId);
        return of(eventMapper.toEventFullDto(eventRepository.save(newEvent)));
    }

    private Event getEventFromRepository(Long eventId) {
        log.info("Получить событие с id {} из репозитория", eventId);
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ApiError(HttpStatus.NOT_FOUND, "Событие не найдено",
                    String.format("Не найдено %s c id %s",
                            ER_OBJ, eventId));
        });
    }

    private EventShortDto updateEventInRepository(Event oldEvent, EventShortDto event) {
        log.info("Обновить событие в репозитории");
        oldEvent = eventMapper.fromEventShortDto(event, oldEvent.getConfirmedRequests(),
        oldEvent.getCreatedOn(), oldEvent.getInitiatorId(), oldEvent.getPublishedOn(),
                oldEvent.getState(), oldEvent.getViews(), oldEvent.getLocLat(), oldEvent.getLocLon());
        return eventMapper.toEventShortDto(oldEvent);
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }

    private void validateEventDate(EventShortDto event) {
        log.info("Проверка даты события");
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Поле eventDate указано неверно.",
                    String.format("Error: must be a date in the present or in the future. Value: %s",
                    event.getEventDate().toString()));
        }
    }

    private void validateEventDate(EventAddDto event) {
        log.info("Проверка даты события");
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Поле eventDate указано неверно.",
                    String.format("Error: must be a date in the present or in the future (plus 2 hours). " +
                                    "Value: %s", event.getEventDate().toString()));
        }
    }

    private void validateUserActivation(Long userId) {
        log.info("Проверка акцивации пользователя");
        if (!userService.getUserById(userId).get()
                .getActivation().equals(Boolean.TRUE)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Пользователь не активирован.",
                    String.format("Пользователь с id %s не может выполнить данное действие. " +
                                    "Получите активацию от администратора", userId));
        }
    }

    private void setNewEventState(EventAddDto event) {
        log.info("Проверка и установка статуса события");
        if (event.getRequestModeration().equals(Boolean.FALSE)
                || event.getParticipantLimit() == null || event.getParticipantLimit().equals(0L)) {
            event.setPublishedOn(LocalDateTime.now());
            event.setState(State.PUBLISHED);
        } else {
            event.setState(State.PENDING);
        }
    }

    private void validateEventForPublishing(Long eventId, Event newEvent) {
        log.info("Проверка события перед публикацией");
        Event event = getEventFromRepository(eventId);
        if (newEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Поле eventDate указано неверно.",
                    String.format("Error: must be a date in the present or in the future (plus 1 hour). " +
                                    "Value: %s", event.getEventDate().toString()));
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "У события неверный статус",
                    String.format("Error: событие должно иметь статус %s", State.PENDING));
        }
    }

    private void validateEventForRejecting(Long eventId) {
        log.info("Проверка события для отклонения");
        Event event = getEventFromRepository(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "У события неверный статус",
                    String.format("Error: событие должно иметь статус %s", State.PUBLISHED));
        }
    }

    private void setDefaultFields(Long userId, Event event) {
        log.info("Установка полей события по умолчанию");
        event.setInitiatorId(userId);
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
        log.info("Получение временного интервала в eventService");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS",
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
        log.info("Получение временного интервала в eventService по умолчанию");
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
