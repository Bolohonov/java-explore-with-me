package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.error.ApiError;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestMainService implements RequestService {
    private final RequestRepository requestRepository;
    private final EventService eventService;

    @Override
    public Collection<RequestDto> getUserRequests(Long userId) {
        return RequestMapper.toRequestDto(requestRepository.getRequestsByRequester(userId));
    }

    @Override
    public Optional<RequestDto> addNewRequest(Long userId, Long eventId) {
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(Status.WAITING)
                .build();
        return of(RequestMapper.toRequestDto(requestRepository.save(request)));
    }

    @Override
    public Optional<RequestDto> revokeRequest(Long userId, Long requestId) {
        Request request = getRequestFromRepository(requestId);
        request.setStatus(Status.CANCELED);
        return of(RequestMapper.toRequestDto(request));
    }

    @Override
    public Collection<RequestDto> getRequestsOfEventInitiator(Long initiatorId, Long eventId) {
        validateEventInitiator(initiatorId, eventId);
        return RequestMapper.toRequestDto(requestRepository
                .getRequestsByEvent(eventId));
    }

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

    @Override
    public Optional<RequestDto> rejectRequest(Long userId, Long requestId) {
        Request request = getRequestFromRepository(requestId);
        request.setStatus(Status.CANCELED);
        return of(RequestMapper.toRequestDto(request));
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
