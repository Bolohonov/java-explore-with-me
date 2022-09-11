package ru.practicum;

import ru.practicum.model.EndpointHit;

import java.util.List;

public interface StatisticsService {
    EndpointHit addEndpointHit(EndpointHit endpointHit);
    Long getStats(Long start, Long end, List<String> uris, Boolean unique);
}
