package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventMainService implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> getEvents(Map<String, String> allParams) {
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EventFullDto> getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
        return eventMapper.toEventFullDto(event, userService.getUserById(event.getInitiatorId(),
                categoryService.getCategoryById(event.getCategoryId())));
    }

    @Override
    public Collection<EventShortDto> findEventsByUser(Long userId) {
        return null;
    }

}
