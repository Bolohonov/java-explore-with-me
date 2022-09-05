package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.event.dto.EventFullDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {
    private final EventClient eventClient;

    @GetMapping
    @ResponseStatus(OK)
    public ResponseEntity<Object> findEvents(@RequestParam(name = "text", required = false) String text,
                                             @RequestParam(name = "categories", required = false)
                                               Integer[] categories,
                                             @RequestParam(name = "paid", required = false) Boolean paid,
                                             @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                             @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                               Boolean onlyAvailable,
                                             @RequestParam(name = "sort", required = false)
                                               String sort,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                               Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10")
                                               Integer size) {
        return eventClient.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}
