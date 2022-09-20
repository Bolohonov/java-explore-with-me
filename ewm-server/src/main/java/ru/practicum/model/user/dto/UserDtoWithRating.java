package ru.practicum.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDtoWithRating {
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
     * Статус активации аккаунта
     */
    private Boolean activation;
    /**
     * Рейтинг пользователя
     */
    private Long rating;
}
