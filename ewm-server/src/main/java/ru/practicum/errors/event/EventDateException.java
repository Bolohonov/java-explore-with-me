package ru.practicum.errors.event;

import org.springframework.http.HttpStatus;
import ru.practicum.errors.ApiError;

public class EventDateException extends ApiError {
    public EventDateException(String date) {
        super(HttpStatus.BAD_REQUEST, String.format("Error: must be a date in the present or in the future " +
                "(plus 2 hours). Value: %s", date), "Поле eventDate указано неверно.");
    }
}
