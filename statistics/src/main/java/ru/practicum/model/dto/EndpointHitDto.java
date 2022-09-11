package ru.practicum.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class EndpointHitDto {
    private Long id;

    String app;

    String uri;

    String ip;

    Long timestamp;
}
