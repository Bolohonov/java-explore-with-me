package ru.practicum.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import ru.practicum.model.event.EventDateDeserializer;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventAddDto {
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
     * Полное описание события
     */
    private String description;
    /**
     * Дата и время на которые намечено событие
     */
    @JsonDeserialize(using = EventDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    /**
     * Нужно ли оплачивать участие
     */
    private Boolean paid;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    private Integer participantLimit;
    /**
     * Нужна ли пре-модерация заявок на участие
     */
    private Boolean requestModeration;
    /**
     * Локация
     */
    private Location location;


    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class Location implements Serializable {
        /**
         * Широта локации
         */
        private Double lat;
        /**
         * Долгота локации
         */
        private Double lon;
    }
}
