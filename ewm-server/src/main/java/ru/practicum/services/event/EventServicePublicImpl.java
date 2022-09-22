package ru.practicum.services.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.errors.ApiError;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.mappers.event.EventMapper;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.repository.event.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePublicImpl implements EventServicePublic {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventServiceImpl eventService;

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, String sort,
                                               Integer from, Integer size) {
        log.info("Получен запрос на вывод списка событий");
        Collection<Event> events = eventRepository.getEvents(text,
                eventService.getSetOfParams(categories), paid,
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
        Event event = eventService.getEventFromRepository(eventId);
        if (event.getPublishedOn() == null) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Событие не найдено",
                    String.format("При выполнении %s не найдено событие c id %s",
                            "getPublishedEventById", eventId));
        }
        event.addView();
        return of(eventMapper.toEventFullDto(event));
    }

    private Map<String, LocalDateTime> getAndValidateTimeRangeWithDefault(String rangeStart, String rangeEnd) {
        log.info("Получение временного интервала в eventService по умолчанию");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",
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
        if (sort != null && (sort.equals("EVENT_DATE") || sort.trim().equals("EVENTDATE"))) {
            result = "eventDate";
        }
        if (sort != null && sort.equals("VIEWS")) {
            result = "views";
        }
        if (sort != null && sort.equals("rating")) {
            result = "rating";
        }
        if (sort != null) {
            result = sort;
        }
        return result;
    }
}
