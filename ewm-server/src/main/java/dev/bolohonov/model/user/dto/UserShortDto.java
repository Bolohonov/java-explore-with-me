package dev.bolohonov.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserShortDto {
    /**
     * уникальный идентификатор пользователя
     */
    private Long id;
    /**
     * имя или логин пользователя
     */
    private String name;
}
