package ru.practicum.services.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.errors.ApiError;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.event.dto.EventAddDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.mappers.event.EventMapper;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.EventUpdateDto;
import ru.practicum.model.like.Like;
import ru.practicum.model.user.dto.UserDtoWithRating;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.like.LikeRepository;
import ru.practicum.services.user.UserService;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePrivateImpl implements EventServicePrivate {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventService eventService;
    private final UserService userService;
    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<EventFullDto> getEventById(Long eventId) {
        log.info("Получен запрос на поиск события");
        Event event = eventService.getEventFromRepository(eventId);
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
    public Optional<EventFullDto> updateEventByInitiator(Long userId, EventUpdateDto event) {
        log.info("Получен запрос в сервис на обновление события инициатором");
        Event oldEvent = eventService.getEventFromRepository(event.getEventId());
        validateEventBeforeUpdateByInitiator(userId, event);
        return of(updateEventInRepository(oldEvent, event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> addEvent(Long userId, EventAddDto event) {
        log.info("Получен запрос в сервис на добавление события");
        validateUserActivation(userId);
        validateEventDate(event);
        Event newEvent = eventMapper.fromEventAddDto(event, userId);
        return of(eventMapper.toEventFullDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> changeEventStateToCanceled(Long userId, Long eventId) {
        log.info("Получен запрос на отмену события");
        Event event = eventService.getEventFromRepository(eventId);
        if (!event.getState().equals(State.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        event.setState(State.CANCELED);
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> addLike(Long userId, Long eventId, Boolean reason) {
        log.info("Запрос в сервис на добавление лайка/дизлайка");
        Event event = eventService.getEventFromRepository(eventId);
        List<ApiError> errorsList = new ArrayList<>();
        if (event == null) {
            errorsList.add(new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при сохранении like",
                    String.format("Проверьте указанные id события %s", eventId)));
        }
        if (!userService.getUserById(userId).isPresent()) {
            errorsList.add(new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при сохранении like",
                    String.format("Проверьте указанные id пользователя %s", userId)));
        }
        if (errorsList.isEmpty()) {
            Like like = likeRepository.findByUserIdAndEventId(userId, eventId);
            if (like != null) {
                checkLikeStatus(event, like, reason);
            } else {
                likeRepository.save(new Like(userId, eventId, reason));
            }
        } else {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при сохранении like",
                    "Получены следующие ошибки при сохранении", errorsList);
        }
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional
    @Override
    public Collection<UserDtoWithRating> getUsersByRating(Long minEventRating, Integer from, Integer size) {
        log.info("Запрос в сервис на получение рейтинга инициаторов событий");
        Collection<Event> events = eventRepository.getEventsByRatingGroupByInitiators(minEventRating, from, size);
        return Collections.emptyList(); //TODO
    }

    private void checkLikeStatus(Event event, Like like, Boolean reason) {
        if (like.getReason().equals(Boolean.TRUE) && reason.equals(Boolean.TRUE)) {
            likeRepository.delete(like);
            getEventById(event.getId());
            log.info("Рейтинг события с id {} обновлен", event.getId());
            return;
        }
        if (like.getReason().equals(Boolean.TRUE) && reason.equals(Boolean.FALSE)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при сохранении like",
                    String.format("Вы уже поставили лайк данному событию с id %s",
                            event.getId()));
        }
        if (like.getReason().equals(Boolean.FALSE) && reason.equals(Boolean.TRUE)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при сохранении like",
                    String.format("Вы уже поставили дизлайк данному событию с id %s",
                            event.getId()));
        }
        if (like.getReason().equals(Boolean.FALSE) && reason.equals(Boolean.FALSE)) {
            likeRepository.delete(like);
            getEventById(event.getId());
            log.info("Рейтинг события с id {} обновлен", event.getId());
        }
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }

    private void validateEventBeforeUpdateByInitiator(Long userId, EventUpdateDto newEvent) {
        List<ApiError> errorsList = new ArrayList<>();
        EventFullDto event = getEventById(newEvent.getEventId()).get();
        try {
            validateEventDate(event);
        } catch (ApiError error) {
            errorsList.add(error);
        }
        if (!event.getInitiator().getId().equals(userId)) {
            errorsList.add(new ApiError(HttpStatus.BAD_REQUEST, String.format("При выполнении %s произошла ошибка.",
                    "update"), String.format("Пользователь с id %s не является инициатором события. Отказано в доступе",
                    userId)));
        }
        if (!event.getState().equals(State.PENDING)
                && !event.getState().equals(State.CANCELED)) {
            errorsList.add(new ApiError(HttpStatus.BAD_REQUEST, String.format("При выполнении %s произошла ошибка.",
                    "update"), String.format("Событие с id %s уже имеет статус Опубликовано. Отказано в доступе",
                    event.getId())));
        }
        if (!errorsList.isEmpty()) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при обновлении события",
                    String.format("Во время обновления события %s произошли ошибки:",
                            event.getId()), errorsList);
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

    private void validateEventDate(EventFullDto event) {
        log.info("Проверка даты события");
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Поле eventDate указано неверно.",
                    String.format("Error: must be a date in the present or in the future (plus 2 hours). " +
                            "Value: %s", event.getEventDate().toString()));
        }
    }

    private EventFullDto updateEventInRepository(Event oldEvent, EventUpdateDto event) {
        log.info("Обновить событие в репозитории");
        oldEvent = eventMapper.fromEventUpdateDtoToUpdate(event, oldEvent, oldEvent.getConfirmedRequests(),
                oldEvent.getCreatedOn(), oldEvent.getInitiatorId(), oldEvent.getPublishedOn(),
                oldEvent.getState(), oldEvent.getViews());
        return eventMapper.toEventFullDto(oldEvent);
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

    private void setNewEventState(Event event) {
        log.info("Проверка и установка статуса события");
        if (event.getRequestModeration().equals(Boolean.FALSE)
                || event.getParticipantLimit() == null || event.getParticipantLimit() == 0L) {
            event.setPublishedOn(LocalDateTime.now());
            event.setState(State.PUBLISHED);
        } else {
            event.setState(State.PENDING);
        }
    }
}
