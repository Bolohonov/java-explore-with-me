package ru.practicum.services.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.errors.ApiError;
import ru.practicum.model.event.Event;
import ru.practicum.repository.event.EventRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventMainService {
    private final EventRepository eventRepository;

    Event getEventFromRepository(Long eventId) {
        log.info("Получить событие с id {} из репозитория", eventId);
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ApiError(HttpStatus.NOT_FOUND, "Событие не найдено",
                    String.format("Не найдено событие c id %s", eventId
                    ));
        });
    }

    <T> Set<T> getAndValidateParams(Class<T> type, List<T> list) {
        Set<T> set = new HashSet<>();
        if (list != null) {
            set.addAll(list);
        }
        return set;
    }
}
