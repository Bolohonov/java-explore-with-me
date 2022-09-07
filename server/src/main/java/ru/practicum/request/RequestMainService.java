package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
    private final RequestMapper requestMapper;
    private final EventService eventService;

    @Override
    public Collection<RequestDto> getUserRequests(Long userId) {
        return requestMapper.toRequestDto(requestRepository.getRequestsByRequester(userId));
    }

    @Override
    public Optional<RequestDto> addNewRequest(Long userId, Long eventId) {
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(Status.WAITING)
                .build();
        return of(requestMapper.toRequestDto(requestRepository.save(request)));
    }

    @Override
    public Optional<RequestDto> revokeRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        request.setStatus(Status.CANCELED);
        return of(requestMapper.toRequestDto(request));
    }

    @Override
    public Optional<RequestDto> getUserRequestOfEvent(Long userId, Long eventId) {
        return of(requestMapper.toRequestDto(requestRepository
                .getRequestByRequesterAndEvent(userId, eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))));
    }

    @Override
    public Optional<RequestDto> confirmRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        EventFullDto event = eventService.getEventById(request.getEvent()).get();
        if (event.getConfirmedRequests() < event.getParticipantLimit()) {
            request.setStatus(Status.ACCEPTED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            Collection<Request> requests = requestRepository.getRequestsByEventAndStatusWaiting(event.getId());
            requests.forEach((r) -> r.setStatus(Status.CANCELED));
        }
        return of(requestMapper.toRequestDto(request));
    }

    @Override
    public Optional<RequestDto> rejectRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        request.setStatus(Status.CANCELED);
        return of(requestMapper.toRequestDto(request));
    }

}
