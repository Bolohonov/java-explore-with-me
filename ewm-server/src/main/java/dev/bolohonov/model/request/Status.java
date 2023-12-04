package dev.bolohonov.model.request;

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
     * Заявка отклонена
     */
    REJECTED,
    /**
     * В архиве
     */
    CANCELED
}
