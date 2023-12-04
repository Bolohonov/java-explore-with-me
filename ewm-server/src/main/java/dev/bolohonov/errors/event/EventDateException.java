package dev.bolohonov.errors.event;

import dev.bolohonov.errors.ApiError;
import org.springframework.http.HttpStatus;

public class EventDateException extends ApiError {
    public EventDateException(String date) {
        super(HttpStatus.BAD_REQUEST, String.format("Error: must be a date in the present or in the future " +
                "(plus 2 hours). Value: %s", date), "Поле eventDate указано неверно.");
    }
}
