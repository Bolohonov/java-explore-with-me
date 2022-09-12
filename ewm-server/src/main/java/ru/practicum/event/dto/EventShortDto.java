package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.State;

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
    private LocalDateTime eventDate;
    /**
     * Инициатор события
     */
    private UserShortDto initiator;
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
