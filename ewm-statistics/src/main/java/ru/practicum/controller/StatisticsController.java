package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.StatisticService;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndpointHitDto;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatisticsController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    @ResponseStatus(OK)
    public EndpointHitDto addNewEndpointHit(
            @RequestBody @Valid EndpointHit endpointHit) {
        log.info("Получен запрос в контроллер на сохранение EndpointHit");
        return statisticService.addEndpointHit(endpointHit).orElseThrow(
                () -> new ResponseStatusException(BAD_REQUEST)
        );
    }

    @GetMapping("/stats")
    @ResponseStatus(OK)
    public Collection<ViewStats> getStatistics(@RequestParam Long start,
                                               @RequestParam Long end,
                                               @RequestParam List<String> uris,
                                               @RequestParam Boolean unique) {
        log.info("Получен запрос в контроллер на получение статистики");
        return statisticService.getStats(start, end, uris, unique);
    }
}
