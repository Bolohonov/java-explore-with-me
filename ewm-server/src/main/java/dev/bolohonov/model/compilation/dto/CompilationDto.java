package dev.bolohonov.model.compilation.dto;

import lombok.*;
import dev.bolohonov.model.event.Event;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
public class CompilationDto {
    /**
     * уникальный идентификатор
     */
    private Long id;
    /**
     * Заголовок события
     */
    @NotBlank
    private String title;
    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;
    /**
     * Список событий, входящих в подборку
     */
    private Set<Event> events;
}
