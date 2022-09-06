package ru.practicum.request;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Request {
    @Builder
    public Request(LocalDateTime created, Long event, Long requester, Status status) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
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
