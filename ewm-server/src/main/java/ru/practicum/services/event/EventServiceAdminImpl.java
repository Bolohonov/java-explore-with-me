package ru.practicum.services.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.errors.ApiError;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.event.dto.EventAddDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.mappers.event.EventMapper;
import ru.practicum.repository.event.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceAdminImpl implements EventServiceAdmin {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventServiceImpl eventService;

    @Transactional
    @Override
    public Collection<EventFullDto> findEvents(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("Получен запрос на вывод списка событий администратором");
        Collection<Event> events = eventRepository.getEventsByAdmin(
                eventService.getSetOfParams(users),
                eventService.getSetOfParams(states),
                eventService.getSetOfParams(categories),
                getAndValidateTimeRange(rangeStart, rangeEnd),
                from,
                size
        );
        return eventMapper.toEventFullDto(events);
    }

    @Transactional
    @Override
    public Optional<EventAddDto> updateEvent(Long eventId, EventAddDto newEventDto) {
        log.info("Получен запрос на обновление события администратором");
        Event oldEvent = eventService.getEventFromRepository(eventId);
        Event newEvent = eventMapper.fromEventAddDtoToUpdate(newEventDto, oldEvent, oldEvent.getConfirmedRequests(),
                oldEvent.getCreatedOn(), oldEvent.getInitiatorId(), oldEvent.getPublishedOn(),
                oldEvent.getState(), oldEvent.getViews());
        newEvent.setId(oldEvent.getId());
        return of(eventMapper.toEventAddDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> publishEvent(Long eventId) {
        log.info("Получен запрос на публикацию события администратором");
        Event event = eventService.getEventFromRepository(eventId);
        validateEventForPublishing(event);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PUBLISHED);
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> rejectEvent(Long eventId) {
        log.info("Получен запрос на отмену события администратором");
        Event event = eventService.getEventFromRepository(eventId);
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
        if (!event.getState().equals(State.PENDING)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "У события неверный статус",
                    String.format("Error: событие должно иметь статус %s", State.PUBLISHED));
        }
    }
}
