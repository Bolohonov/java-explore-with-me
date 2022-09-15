package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "hits", schema = "public")
@Getter
@Setter
@ToString
public class EndpointHit {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "app", nullable = false)
    String app;

    @NotNull
    @Column(name = "uri", nullable = false)
    String uri;

    @NotNull
    @Column(name = "ip", nullable = false)
    String ip;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    String timestamp;
}
