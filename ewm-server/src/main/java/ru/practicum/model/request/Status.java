package ru.practicum.model.request;

public enum Status {
    /**
     * Ожидает утверждения
     */
    WAITING,
    /**
     * Запрос подтвержден
     */
    CONFIRMED,
    /**
     * В архиве
     */
    REJECTED
}
