package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHit;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService implements StatisticsService {
    @Override
    public EndpointHit addEndpointHit(EndpointHit endpointHit) {
        return null;
    }

    @Override
    public Long getStats(Long start, Long end, List<String> uris, Boolean unique) {
        return null;
    }
}
