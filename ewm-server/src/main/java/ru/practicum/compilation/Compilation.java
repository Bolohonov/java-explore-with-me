package ru.practicum.compilation;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.event.Event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

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

    @ManyToMany(mappedBy = "comps")
    private Set<Event> events = new HashSet<>();

//    public Compilation() {
//        this.events_in = this.events.stream().mapToLong(Event::getId).collect(Collectors.toList());
//    }
}
