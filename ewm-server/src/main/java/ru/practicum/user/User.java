package ru.practicum.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import javax.validation.constraints.Email;

/**
 * класс с описанием пользователя - User //
 */
@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@ToString
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
    /**
     * Статус активации аккаунта
     */
    private Boolean activation;
}
