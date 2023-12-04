package dev.bolohonov.model.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Data
@AllArgsConstructor
public class CompilationAddDto {
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
     * Список идентифкаторов событий, входящих в подборку
     */
    private Collection<Long> events;
}
