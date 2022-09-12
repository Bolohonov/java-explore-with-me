package ru.practicum.compilation.dto;

import lombok.*;
import java.util.Collection;

@Data
@Builder
public class CompilationDto {
    /**
     * уникальный идентификатор
     */
    private Long id;
    /**
     * Заголовок события
     */
    private String title;
    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;
    /**
     * Список идентификаторов событий входящих в подборку
     */
    private Collection<Long> events;
}
