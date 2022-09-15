package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.ApiError;
import ru.practicum.event.Event;
import ru.practicum.event.State;
import ru.practicum.event.dto.EventAddDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAdminService implements EventServiceAdmin {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventMainService eventMainService;

    @Transactional
    @Override
    public Collection<EventFullDto> findEvents(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("Получен запрос на вывод списка событий администратором");
        Collection<Event> events = eventRepository.getEventsByAdmin(
                eventMainService.getAndValidateParams(Long.class, users),
                eventMainService.getAndValidateParams(String.class, states),
                eventMainService.getAndValidateParams(Long.class, categories),
                getAndValidateTimeRange(rangeStart, rangeEnd),
                from,
                size
        );
        return eventMapper.toEventFullDto(events);
    }

    @Transactional
    @Override
    public Optional<EventShortDto> updateEvent(Long eventId, Event newEvent) {
        log.info("Получен запрос на обновление списка событий администратором");
        eventMainService.getEventFromRepository(eventId);
        newEvent.setId(eventId);
        return of(eventMapper.toEventShortDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> publishEvent(Long eventId) {
        log.info("Получен запрос на публикацию события администратором");
        Event event = eventMainService.getEventFromRepository(eventId);
        validateEventForPublishing(event);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PUBLISHED);
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> rejectEvent(Long eventId) {
        log.info("Получен запрос на отмену события администратором");
        Event event = eventMainService.getEventFromRepository(eventId);
        validateEventForRejecting(event);
        event.setState(State.CANCELED);
        return of(eventMapper.toEventFullDto(event));
    }

    private Map<String, LocalDateTime> getAndValidateTimeRange(String rangeStart, String rangeEnd) {
        log.info("Получение временного интервала в eventService");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
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

    private void validateEventForPublishing(Event event) {
        log.info("Проверка события перед публикацией");
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Поле eventDate указано неверно.",
                    String.format("Error: must be a date in the present or in the future (plus 1 hour). " +
                            "Value: %s", event.getEventDate().toString()));
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "У события неверный статус",
                    String.format("Error: событие должно иметь статус %s", State.PENDING));
        }
    }

    private void validateEventForRejecting(Event event) {
        log.info("Проверка события для отклонения");
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "У события неверный статус",
                    String.format("Error: событие должно иметь статус %s", State.PUBLISHED));
        }
    }
}
