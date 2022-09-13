package ru.practicum.category;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "categories", schema = "public")
@Data
public class Category {
    /**
     * уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * название категории
     */
    private String name;
}
