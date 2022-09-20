package ru.practicum.model.request;

public enum Status {
    /**
     * Ожидает утверждения
     */
    PENDING,
    /**
     * Запрос подтвержден
     */
    CONFIRMED,
    /**
     * В архиве
     */
    REJECTED
}
