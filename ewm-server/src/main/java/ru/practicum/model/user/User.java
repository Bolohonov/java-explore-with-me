package ru.practicum.model.user;

import lombok.Data;

import javax.persistence.*;

/**
 * класс с описанием пользователя - User //
 */
@Entity
@Table(name = "users", schema = "public")
@Data
public class User {
    /**
     * уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * имя или логин пользователя
     */
    private String name;
    /**
     * адрес электронной почты
     */
    private String email;
}
