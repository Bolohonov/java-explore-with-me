package ru.practicum.model.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.dto.EndpointHitDto;

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
