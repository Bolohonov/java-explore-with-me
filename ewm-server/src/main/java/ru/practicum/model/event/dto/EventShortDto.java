package ru.practicum.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventShortDto {
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
    private CategoryDto category;
    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private Long confirmedRequests;
    /**
     * Дата и время на которые намечено событие
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_GB")
    private LocalDateTime eventDate;
    /**
     * Инициатор события
     */
    private EventShortDto.UserShortDto initiator;
    /**
     * Нужно ли оплачивать участие
     */
    private Boolean paid;
    /**
     * Количество просмотрев события
     */
    private Long views;

    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class CategoryDto implements Serializable {
        /**
         * уникальный идентификатор
         */
        private Long id;
        /**
         * название категории
         */
        private String name;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class UserShortDto implements Serializable {
        /**
         * уникальный идентификатор
         */
        private Long id;
        /**
         * название категории
         */
        private String name;
    }
}
