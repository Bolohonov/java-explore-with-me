package ru.practicum.event;

public enum State {
    /**
     * Ожидает утверждения
     */
    PENDING,
    /**
     * Опубликовано
     */
    PUBLISHED,
    /**
     * В архиве
     */
    CANCELED;
}
