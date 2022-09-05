package ru.practicum.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@ToString
public class Request {
    /**
     * уникальный идентификатор заявки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
