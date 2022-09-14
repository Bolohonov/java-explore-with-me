package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.event.State;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class EventAddDto {
    /**
     * уникальный идентификатор
     */
    private Long id;
    /**
     * Заголовок события
     */
    private String title;
    /**
     * Краткое описание
     */
    private String annotation;
    /**
     * Категория
     */
    private Long category;
    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private Long confirmedRequests;
    /**
     * Дата и время создания события
     */
    private LocalDateTime createdOn;
    /**
     * Полное описание события
     */
    private String description;
    /**
     * Дата и время на которые намечено событие
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime eventDate;
    /**
     * Инициатор события
     */
    private Long initiator;
    /**
     * Нужно ли оплачивать участие
     */
    private Boolean paid;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    private Integer participantLimit;
    /**
     * Дата и время публикации события
     */
    private LocalDateTime publishedOn;
    /**
     * Нужна ли пре-модерация заявок на участие
     */
    private Boolean requestModeration;
    /**
     * Список состояний жизненного цикла события
     */
    private State state;
    /**
     * Количество просмотрев события
     */
    private Long views;
    /**
     * Локация
     */
    private EventFullDto.Location location;


    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class Location implements Serializable {
        /**
         * Широта локации
         */
        Double lat;
        /**
         * Долгота локации
         */
        Double lon;
    }
}
