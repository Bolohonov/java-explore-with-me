package dev.bolohonov.model.event;

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
    CANCELED
}
