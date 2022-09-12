package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.ApiError;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final ApiError e, HttpServletResponse response) {
        List<String> err = new ArrayList<>();
        response.setStatus(e.getStatus().value());
        for (StackTraceElement stack : e.getStackTrace()) {
            err.add("Класс: " + stack.getClassName() + ", " + "метод: " + stack.getMethodName());
        }
        return ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .errors(err)
                .message(e.getClass().toString())
                .reason(e.getMessage())
                .status(e.getStatus().toString())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}