package dev.bolohonov.model.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import dev.bolohonov.model.event.Event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "compilations", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private Set<Event> events;

    public Compilation(String title, Boolean pinned, Set<Event> events) {
        this.title = title;
        this.pinned = pinned;
        this.events = events;
    }
}
