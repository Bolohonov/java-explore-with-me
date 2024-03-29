package dev.bolohonov.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "events", schema = "public")
@RequiredArgsConstructor
@AllArgsConstructor
public class Event {
    /**
     * уникальный идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Заголовок события
     */
    @Column(name = "title")
    private String title;
    /**
     * Краткое описание
     */
    @Column(name = "annotation")
    private String annotation;
    /**
     * Категория
     */
    @Column(name = "category_id")
    private Long category;
    /**
     * Количество одобренных заявок на участие в данном событии
     */
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    /**
     * Дата и время создания события
     */
    @Column(name = "created_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    /**
     * Полное описание события
     */
    @Column(name = "description")
    private String description;
    /**
     * Дата и время на которые намечено событие
     */
    @Column(name = "event_date")
    @JsonDeserialize(using = EventDateDeserializer.class)
    private LocalDateTime eventDate;
    /**
     * Инициатор события
     */
    @Column(name = "initiator_id")
    private Long initiatorId;
    /**
     * Нужно ли оплачивать участие
     */
    @Column(name = "paid")
    private Boolean paid;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    @Column(name = "participant_limit")
    private Integer participantLimit;
    /**
     * Дата и время публикации события
     */
    @Column(name = "published_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    /**
     * Нужна ли пре-модерация заявок на участие
     */
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    /**
     * Список состояний жизненного цикла события
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;
    /**
     * Количество просмотрев события
     */
    @Column(name = "views")
    private Long views;
    /**
     * Широта локации
     */
    @Column(name = "loc_lat")
    private Double locLat;
    /**
     * Долгота локации
     */
    @Column(name = "loc_lon")
    private Double locLon;
    /**
     * Рейтинг события
     */
    @Column(name = "rating")
    private Long rating;

    public Event(String title, String annotation, Long category, Long confirmedRequests,
                 LocalDateTime createdOn, String description, LocalDateTime eventDate, Long initiatorId,
                 Boolean paid, Integer participantLimit, LocalDateTime publishedOn, Boolean requestModeration,
                 State state, Long views, Double locLat, Double locLon, Long rating) {
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiatorId = initiatorId;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.views = views;
        this.locLat = locLat;
        this.locLon = locLon;
        this.rating = rating;
    }

    public void addView() {
        if (views != null) {
            ++views;
        } else {
            views = 1L;
        }
    }
}
