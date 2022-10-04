package ru.practicum.errors.user;

import org.springframework.http.HttpStatus;
import ru.practicum.errors.ApiError;

public class UserNotFoundException extends ApiError {
    public UserNotFoundException(String reason, Long id) {
        super(HttpStatus.BAD_REQUEST, String.format("Пользователь с id %s не найден", id), reason);
    }
}
