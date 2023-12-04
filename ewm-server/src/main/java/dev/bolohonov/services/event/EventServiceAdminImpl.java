package dev.bolohonov.services.event;

import dev.bolohonov.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.bolohonov.errors.ApiError;
import dev.bolohonov.model.event.Event;
import dev.bolohonov.model.event.State;
import dev.bolohonov.model.event.dto.EventAddDto;
import dev.bolohonov.model.event.dto.EventFullDto;
import dev.bolohonov.mappers.event.EventMapper;

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
        log.debug("Получен запрос на вывод списка событий администратором");
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
        log.debug("Получен запрос на обновление события администратором");
        Event oldEvent = eventService.getEventFromRepository(eventId);
        Event newEvent = eventMapper.fromEventUpdateDtoToUpdate(newEventDto, oldEvent, oldEvent.getConfirmedRequests(),
                oldEvent.getCreatedOn(), oldEvent.getInitiatorId(), oldEvent.getPublishedOn(),
                oldEvent.getState(), oldEvent.getViews());
        newEvent.setId(oldEvent.getId());
        return of(eventMapper.toEventAddDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> publishEvent(Long eventId) {
        log.debug("Получен запрос на публикацию события администратором");
        Event event = eventService.getEventFromRepository(eventId);
        validateEventForPublishing(event);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PUBLISHED);
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> rejectEvent(Long eventId) {
        log.debug("Получен запрос на отмену события администратором");
        Event event = eventService.getEventFromRepository(eventId);
        validateEventForRejecting(event);
        event.setState(State.CANCELED);
        return of(eventMapper.toEventFullDto(event));
    }

    private Map<String, LocalDateTime> getAndValidateTimeRange(String rangeStart, String rangeEnd) {
        log.debug("Получение временного интервала в eventService");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",
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
        log.debug("Проверка события перед публикацией");
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
        log.debug("Проверка события для отклонения");
        if (!event.getState().equals(State.PENDING)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "У события неверный статус",
                    String.format("Error: событие должно иметь статус %s", State.PUBLISHED));
        }
    }
}
