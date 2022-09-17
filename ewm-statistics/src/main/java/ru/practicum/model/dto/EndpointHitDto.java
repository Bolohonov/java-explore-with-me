package ru.practicum.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class EndpointHitDto {
    private Long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
