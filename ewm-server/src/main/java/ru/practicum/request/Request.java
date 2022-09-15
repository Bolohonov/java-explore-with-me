package ru.practicum.request;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Data
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
    @Column(name = "created")
    private LocalDateTime created;
    /**
     * Идентификатор события
     */
    @Column(name = "event_id")
    private Long event;
    /**
     * Идентификатор пользователя, отправившего заявку
     */
    @Column(name = "requester_id")
    private Long requester;
    /**
     * Статус заявки
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
