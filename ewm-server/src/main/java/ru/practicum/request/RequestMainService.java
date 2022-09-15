package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.ApiError;
import ru.practicum.event.EventService;
import ru.practicum.event.State;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestMainService implements RequestService {
    private final RequestRepository requestRepository;
    private final EventService eventService;

    @Transactional(readOnly = true)
    @Override
    public Collection<RequestDto> getUserRequests(Long userId) {
        return RequestMapper.toRequestDto(requestRepository.getRequestsByRequester(userId));
    }

    @Transactional
    @Override
    public Optional<RequestDto> addNewRequest(Long userId, Long eventId) {
        validateRequestForAdd(userId, eventId);
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(Status.WAITING)
                .build();
        validateAndSetStatus(eventId, request);
        return of(RequestMapper.toRequestDto(requestRepository.save(request)));
    }

    @Transactional
    @Override
    public Optional<RequestDto> revokeRequest(Long userId, Long requestId) {
        Request request = getRequestFromRepository(requestId);
        request.setStatus(Status.CANCELED);
        return of(RequestMapper.toRequestDto(request));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<RequestDto> getRequestsOfEventInitiator(Long initiatorId, Long eventId) {
        validateEventInitiator(initiatorId, eventId);
        return RequestMapper.toRequestDto(requestRepository
                .getRequestsByEvent(eventId));
    }

    @Transactional
    @Override
    public Optional<RequestDto> confirmRequest(Long userId, Long requestId) {
        Request request = getRequestFromRepository(requestId);
        EventFullDto event = eventService.getEventById(request.getEvent()).get();
        if (event.getConfirmedRequests() < event.getParticipantLimit()) {
            request.setStatus(Status.ACCEPTED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit().longValue())) {
            Collection<Request> requests = requestRepository.getRequestsByEventAndStatus(event.getId(),
                    Status.WAITING.toString());
            requests.forEach((r) -> r.setStatus(Status.CANCELED));
        }
        return of(RequestMapper.toRequestDto(request));
    }

    @Transactional
    @Override
    public Optional<RequestDto> rejectRequest(Long userId, Long requestId) {
        Request request = getRequestFromRepository(requestId);
        request.setStatus(Status.CANCELED);
        return of(RequestMapper.toRequestDto(request));
    }

    private void validateRequestForAdd(Long userId, Long eventId) {
        log.info("Проверка события и запроса");
        EventFullDto event = eventService.getEventById(eventId).get();
        List<ApiError> errors = new ArrayList<>();
        if (requestRepository.getRequestByRequesterAndEvent(userId, eventId) != null) {
            errors.add(new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "Ошибка запроса на участие",
                            String.format("Пользователь с id %s уже отправил запрос на участие в событии с id %s",
                                    userId, eventId)
                    )
            );
        }
        if (event.getInitiator().getId().equals(userId)) {
            errors.add(new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "Ошибка запроса на участие",
                            String.format("Пользователь с id %s является инициатором события с id %s. " +
                                            "Запрос на участие не требуется",
                                    userId, eventId)
                    )
            );
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            errors.add(new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "Ошибка запроса на участие",
                            String.format("Событие с id %s еще не опубликовано", eventId)
                    )
            );
        }
        if (event.getConfirmedRequests() != null
                && !event.getConfirmedRequests().equals(event.getParticipantLimit().longValue())) {
            errors.add(new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "Ошибка запроса на участие",
                            String.format("В событии с id %s достигнут лимит участников", eventId)
                    )
            );
        }
        if (!errors.isEmpty()) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Ошибка запроса на участие",
                    "Получены ошибки при проверке условий запроса", errors);
        }
        log.info("Запрос прошел проверки");
    }

    private void validateAndSetStatus(Long eventId, Request request) {
        log.info("Установка статуса запроса");
        if (!eventService.getEventById(eventId).get().getRequestModeration()) {
            request.setStatus(Status.ACCEPTED);
            log.info("Установлен статус ACCEPTED");
        }
    }

    private void validateEventInitiator(Long initiatorId, Long eventId) {
        if (!eventService.getEventById(eventId)
                .get()
                .getInitiator()
                .getId()
                .equals(initiatorId)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Действие не может быть выполнено",
                    String.format("Пользователь с %s не является инициатором события c id %s. " +
                            "Действие отклонено.", initiatorId, eventId));
        }
    }

    private Request getRequestFromRepository(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            throw new ApiError(HttpStatus.NOT_FOUND, "Запрос не найден",
                    String.format("При выполнении метода не найден %s c id %s. Проверьте id.", requestId));
        });
    }
}
