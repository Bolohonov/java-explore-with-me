package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    /**
     * уникальный идентификатор пользователя
     */
    private Long id;
    /**
     * название категории
     */
    private String name;
}
