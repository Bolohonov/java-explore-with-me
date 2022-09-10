package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatisticService;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/hit")
@Validated
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping
    @ResponseStatus(OK)
    public Optional<CompilationDto> addNewCompilation(
            @RequestBody @Valid Compilation compilation) {
        return compilationService.addCompilation(compilation);
    }
}
