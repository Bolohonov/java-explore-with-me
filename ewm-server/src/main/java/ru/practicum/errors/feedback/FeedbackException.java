package ru.practicum.errors.feedback;

import org.springframework.http.HttpStatus;
import ru.practicum.errors.ApiError;

public class FeedbackException extends ApiError {
    public FeedbackException(String message) {
        super(HttpStatus.BAD_REQUEST, message, "Ошибка при сохранении feedback");

    }

    public static FeedbackException dislikeAlreadyExists(long id) {
        return new FeedbackException(String.format("Вы уже поставили дизлайк данному событию с id %s", id));
    }

    public static FeedbackException likeAlreadyExists(long id) {
        return new FeedbackException(String.format("Вы уже поставили лайк данному событию с id %s", id));
    }
}
