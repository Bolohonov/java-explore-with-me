package ru.practicum.client.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class EndpointHitDto {
    String app;

    String uri;

    String ip;

    String timestamp;
}
