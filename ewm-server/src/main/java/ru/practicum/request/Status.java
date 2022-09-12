package ru.practicum.request;

public enum Status {
    /**
     * Ожидает утверждения
     */
    WAITING,
    /**
     * Запрос подтвержден
     */
    ACCEPTED,
    /**
     * В архиве
     */
    CANCELED
}
