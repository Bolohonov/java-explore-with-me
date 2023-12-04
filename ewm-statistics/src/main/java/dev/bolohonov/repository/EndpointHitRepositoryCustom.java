package dev.bolohonov.repository;

import dev.bolohonov.model.EndpointHit;

import java.util.Collection;
import java.util.List;

public interface EndpointHitRepositoryCustom {
    /**
     * Получить статистику за период от start до end с выбранным списком uris
     */
    Collection<EndpointHit> getEndpointHits(Long start, Long end, List<String> uris);
}
