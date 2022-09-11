package ru.practicum;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndpointHitDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StatisticsService {
    Optional<EndpointHitDto> addEndpointHit(EndpointHit endpointHit);
    Collection<ViewStats> getStats(Long start, Long end, List<String> uris, Boolean unique);
}
