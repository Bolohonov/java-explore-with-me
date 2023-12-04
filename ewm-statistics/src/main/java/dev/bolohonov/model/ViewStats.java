package dev.bolohonov.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ViewStats {
    /**
     * Название сервиса
     */
    private String app;
    /**
     * URI сервиса
     */
    private String uri;
    /**
     * Количество просмотров
     */
    private Long hits;
}
