package ru.practicum.compilation;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compilations", schema = "public")
@Data
@TypeDef(
        name = "list-array",
        typeClass = ArrayList.class
)
public class Compilation {
    /**
     * уникальный идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Заголовок события
     */
    @Column(name = "title")
    @NotBlank
    private String title;
    /**
     * Закреплена ли подборка на главной странице сайта
     */
    @Column(name = "pinned")
    private Boolean pinned;
    /**
     * Список идентификаторов событий входящих в подборку
     */
    @ElementCollection
    @Type(type = "list-array")
    private List<Long> events_in;
}
