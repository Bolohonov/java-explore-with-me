package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.Event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "compilations", schema = "public")
@Data
@AllArgsConstructor
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

    @ManyToMany
    @JoinTable(name = "events_in_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events = new HashSet<>();
}
