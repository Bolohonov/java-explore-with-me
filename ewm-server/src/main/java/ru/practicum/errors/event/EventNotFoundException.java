package ru.practicum.errors.event;

import org.springframework.http.HttpStatus;
import ru.practicum.errors.ApiError;

public class EventNotFoundException extends ApiError {
    public EventNotFoundException(String reason, Long id) {
        super(HttpStatus.BAD_REQUEST, String.format("Событие с id %s не найдено", id), reason);
    }
}
