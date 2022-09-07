package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.user.exceptions.UserNotFoundException;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class ErrorHandler {
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleAccessToItemException(final AccessToItemException e) {
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleAccessToBookingException(final AccessToBookingException e) {
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
//
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        List<String> err = new ArrayList<>();
        for (StackTraceElement stack : e.getStackTrace()) {
            err.add("Класс: " + stack.getClassName() + ", " + "метод: " + stack.getMethodName());
        }
        return ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .errors(err)
                .message(e.getClass().toString())
                .reason(e.getMessage())
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleItemNotFoundException(final ItemNotFoundException e) {
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
}