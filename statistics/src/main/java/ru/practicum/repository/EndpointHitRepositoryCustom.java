package ru.practicum.repository;

import ru.practicum.model.EndpointHit;

import java.util.Collection;
import java.util.List;

public interface EndpointHitRepositoryCustom {
    Collection<EndpointHit> getEndpointHits(Long start, Long end, List<String> uris);
}
