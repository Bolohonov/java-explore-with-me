package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndpointHitDto;
import ru.practicum.model.mapper.EndpointHitMapper;
import ru.practicum.repository.EndpointHitRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticService implements StatisticsService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public Optional<EndpointHitDto> addEndpointHit(EndpointHit endpointHit) {
        log.info("Получен запрос в сервис на сохранение EndpointHit");
        return ofNullable(EndpointHitMapper.toEndpointHitDto(endpointHitRepository.save(endpointHit)));
    }

    @Override
    public Collection<ViewStats> getStats(Long start, Long end, List<String> uris, Boolean unique) {
        log.info("Получен запрос в сервис на получение статистики");
        Collection<EndpointHit> eHits = endpointHitRepository.getEndpointHits(start, end, uris);
        return toViewStats(eHits, unique);
    }

    private Collection<ViewStats> toViewStats(Collection<EndpointHit> eHits, Boolean unique) {
        Collection<ViewStats> results = new ArrayList<>();
        Map<String, List<EndpointHit>> groupByUri = eHits.stream()
                .collect(Collectors.groupingBy(EndpointHit::getUri));
        if (unique) {
            for (Map.Entry<String, List<EndpointHit>> entry : groupByUri.entrySet()) {
                results.add(ViewStats.builder()
                        .app("ExploreWithMe")
                        .uri(entry.getKey())
                        .hits((long) entry.getValue()
                                .stream()
                                .collect(Collectors.groupingBy(EndpointHit::getIp))
                                .size())
                        .build()
                );
            }

        } else {
            groupByUri.entrySet().stream()
                    .forEach(m -> results.add(ViewStats.builder()
                            .app("ExploreWithMe")
                            .uri(m.getKey())
                            .hits((long) m.getValue().size())
                            .build()));
        }
        return results;
    }
}
