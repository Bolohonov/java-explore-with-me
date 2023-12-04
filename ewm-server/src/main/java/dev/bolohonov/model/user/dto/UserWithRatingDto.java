package dev.bolohonov.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithRatingDto {
    /**
     * уникальный идентификатор пользователя
     */
    private Long id;
    /**
     * имя или логин пользователя
     */
    private String name;
    /**
     * адрес электронной почты
     */
    private String email;
    /**
     * Рейтинг пользователя
     */
    private Long rating;
}
