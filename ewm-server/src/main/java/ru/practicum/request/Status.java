package ru.practicum.request;

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
