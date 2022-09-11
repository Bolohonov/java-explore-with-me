package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatisticService;
import ru.practicum.model.EndpointHit;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping()
@Validated
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    @ResponseStatus(OK)
    public EndpointHit addNewEndpointHit(
            @RequestBody @Valid EndpointHit endpointHit) {
        return statisticService.addEndpointHit(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(OK)
    public Long getStatistics(@RequestParam Long start,
                                     @RequestParam Long end,
                                     @RequestParam List<String> uris,
                                     @RequestParam Boolean unique) {
        return statisticService.getStats(start, end, uris, unique);
    }
}
