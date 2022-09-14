package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.compilation.Compilation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "events", schema = "public")
@Data
@EqualsAndHashCode(exclude = "comps")
@NoArgsConstructor
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
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

    public void addView() {
        if (views != null) {
            ++views;
        } else {
            views = 1L;
        }
    }
}
