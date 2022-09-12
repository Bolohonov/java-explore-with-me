package ru.practicum.model.dto;

import org.springframework.stereotype.Component;
import ru.practicum.model.EndpointHit;

@Component
public class EndpointHitMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }
}
