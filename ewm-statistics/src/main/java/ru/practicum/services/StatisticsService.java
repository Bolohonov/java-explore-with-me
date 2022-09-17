package ru.practicum.services;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndpointHitDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StatisticsService {
    /**
     * Добавить информацию по посещенному uri
     */
    Optional<EndpointHitDto> addEndpointHit(EndpointHit endpointHit);

    /**
     * Получить статистику за период от start до end с выбранным списком uris,
     * флаг unique на уникальные/неуникальные записи
     */
    Collection<ViewStats> getStats(Long start, Long end, List<String> uris, Boolean unique);
}
