package dev.bolohonov.model;

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
    /**
     * Идентификатор записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Идентификатор сервиса для которого записывается информация
     */
    @NotNull
    @Column(name = "app", nullable = false)
    private String app;

    /**
     * URI для которого был осуществлен запрос
     */
    @NotNull
    @Column(name = "uri", nullable = false)
    private String uri;

    /**
     * IP-адрес пользователя, осуществившего запрос
     */
    @NotNull
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @NotNull
    @Column(name = "timestamp", nullable = false)
    private String timestamp;
}
