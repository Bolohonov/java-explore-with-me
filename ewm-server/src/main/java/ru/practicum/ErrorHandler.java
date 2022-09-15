package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.ApiError;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final ApiError e, HttpServletResponse response) {
        StringBuffer sb = new StringBuffer();
        e.getSubErrors().forEach(error -> sb.append(error.toString()));
        response.setStatus(e.getStatus().value());
        return ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .errors(sb.toString())
                .message(e.getMessage())
                .reason(e.getReason())
                .status(e.getStatus().toString())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}