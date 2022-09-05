package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.user.exceptions.UserNotFoundException;

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
        return new ErrorResponse(
                e.getMessage()
        );
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