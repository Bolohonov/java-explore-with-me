package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

}
