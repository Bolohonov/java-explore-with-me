package dev.bolohonov.errors.user;

import dev.bolohonov.errors.ApiError;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiError {
    public UserNotFoundException(String reason, Long id) {
        super(HttpStatus.BAD_REQUEST, String.format("Пользователь с id %s не найден", id), reason);
    }
}
