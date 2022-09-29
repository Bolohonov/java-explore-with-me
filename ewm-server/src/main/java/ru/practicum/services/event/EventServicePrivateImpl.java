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
import ru.practicum.errors.event.EventDateException;
import ru.practicum.errors.event.EventNotFoundException;
import ru.practicum.errors.feedback.FeedbackException;
import ru.practicum.errors.user.UserNotFoundException;
import ru.practicum.mappers.user.UserMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.event.dto.EventAddDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.mappers.event.EventMapper;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.EventUpdateDto;
import ru.practicum.model.feedback.Feedback;
import ru.practicum.model.user.dto.UserDto;
import ru.practicum.model.user.dto.UserWithRatingDto;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.feedback.FeedbackRepository;
import ru.practicum.services.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePrivateImpl implements EventServicePrivate {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventService eventService;
    private final UserService userService;
    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<EventFullDto> getEventById(Long eventId) {
        log.debug("Получен запрос на поиск события c id {}", eventId);
        Event event = eventService.getEventFromRepository(eventId);
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> findEventsByInitiator(Long userId, Integer from, Integer size) {
        log.debug("Получен запрос в сервис на поиск события по инициатору");
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
        log.debug("Получен запрос в сервис на обновление события инициатором");
        Event oldEvent = eventService.getEventFromRepository(event.getEventId());
        validateEventBeforeUpdateByInitiator(userId, event);
        return of(updateEventInRepository(oldEvent, event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> addEvent(Long userId, EventAddDto event) {
        validateEventDate(event);
        Event newEvent = eventMapper.fromEventAddDto(event, userId);
        return of(eventMapper.toEventFullDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> changeEventStateToCanceled(Long userId, Long eventId) {
        log.debug("Получен запрос на отмену события c id {}", eventId);
        Event event = eventService.getEventFromRepository(eventId);
        if (!event.getState().equals(State.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        event.setState(State.CANCELED);
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> addLike(Long userId, Long eventId) {
        log.debug("Запрос в сервис на добавление лайка событию с id {}", eventId);
        return of(eventMapper.toEventFullDto(addLikeOrDislike(userId, eventId, Boolean.TRUE)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> addDislike(Long userId, Long eventId) {
        log.debug("Запрос в сервис на добавление дизлайка событию с id {}", eventId);
        return of(eventMapper.toEventFullDto(addLikeOrDislike(userId, eventId, Boolean.FALSE)));
    }

    @Transactional
    @Override
    public Collection<UserWithRatingDto> getUsersByRating(Integer from, Integer size) {
        log.debug("Запрос в сервис на получение рейтинга инициаторов событий");
        Collection<Object[]> queryResult = eventRepository.getEventsByRatingGroupByInitiators(from, size);
        Map<UserDto, Long> usersRating = new HashMap<>();
        for (Object[] o : queryResult) {
            for (int i = 0; i < o.length; i ++) {
                usersRating.put(userService.getUserById(Long.parseLong(o[0].toString())).get(),
                        Long.parseLong(o[1].toString()));
            }
        }
        Map<UserDto,Long> usersRatingPage =
                usersRating.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .skip(from)
                        .limit(usersRating.size() + size - 1)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return UserMapper.toUserDtoWithRating(usersRatingPage);
    }

    public Event addLikeOrDislike(Long userId, Long eventId, Boolean isLike) {
        Event event = eventService.getEventFromRepository(eventId);
        List<ApiError> errorsList = new ArrayList<>();
        if (event == null) {
            errorsList.add(new EventNotFoundException("Ошибка при сохранении feedback", eventId));
        }
        if (!userService.getUserById(userId).isPresent()) {
            errorsList.add(new UserNotFoundException("Ошибка при сохранении feedback", userId));
        }
        if (errorsList.isEmpty()) {
            Feedback feedback = feedbackRepository.findByUserIdAndEventId(userId, eventId);
            if (feedback != null) {
                checkStatusOfLikeDislike(event, feedback, isLike);
            } else {
                feedbackRepository.save(new Feedback(userId, eventId, isLike));
                eventService.getEventFromRepository(eventId);
            }
        } else {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при сохранении feedback",
                    "Получены следующие ошибки при сохранении", errorsList);
        }
        return event;
    }

    private void checkStatusOfLikeDislike(Event event, Feedback feedback, Boolean reason) {
        if (feedback.getIsLike().equals(Boolean.TRUE) && reason.equals(Boolean.FALSE)) {
            FeedbackException.likeAlreadyExists(event.getId());
        }
        if (feedback.getIsLike().equals(Boolean.FALSE) && reason.equals(Boolean.TRUE)) {
            FeedbackException.dislikeAlreadyExists(event.getId());
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
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new EventDateException(event.getEventDate().toString());
        }
    }

    private void validateEventDate(EventFullDto event) {
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new EventDateException(event.getEventDate().toString());
        }
    }

    private EventFullDto updateEventInRepository(Event oldEvent, EventUpdateDto event) {
        log.debug("Обновить событие в репозитории");
        oldEvent = eventMapper.fromEventUpdateDtoToUpdate(event, oldEvent, oldEvent.getConfirmedRequests(),
                oldEvent.getCreatedOn(), oldEvent.getInitiatorId(), oldEvent.getPublishedOn(),
                oldEvent.getState(), oldEvent.getViews());
        return eventMapper.toEventFullDto(oldEvent);
    }
}
