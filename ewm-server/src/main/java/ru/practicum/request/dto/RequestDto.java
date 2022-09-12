package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.request.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestDto {
    /**
     * уникальный идентификатор заявки
     */
    private Long id;
    /**
     * Дата и время создания заявки
     */
    private LocalDateTime created;
    /**
     * Идентификатор события
     */
    private Long event;
    /**
     * Идентификатор пользователя, отправившего заявку
     */
    private Long requester;
    /**
     * Статус заявки
     */
    private Status status;
}
