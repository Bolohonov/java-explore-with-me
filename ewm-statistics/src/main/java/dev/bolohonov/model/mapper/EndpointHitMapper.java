package dev.bolohonov.model.mapper;

import dev.bolohonov.model.EndpointHit;
import dev.bolohonov.model.dto.EndpointHitDto;
import org.springframework.stereotype.Component;

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
