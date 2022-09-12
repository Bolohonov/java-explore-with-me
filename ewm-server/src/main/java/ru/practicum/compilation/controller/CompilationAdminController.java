package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.CompilationService;
import ru.practicum.compilation.dto.CompilationDto;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/compilations")
@Validated
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(OK)
    public Optional<CompilationDto> addNewCompilation(
            @RequestBody @Valid Compilation compilation) {
        return compilationService.addCompilation(compilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(OK)
    public void removeCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    @ResponseStatus(OK)
    public void removeEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId) {
        if (!compilationService.removeEventFromCompilation(compId, eventId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @PatchMapping("/{compId}/events/{eventId}")
    @ResponseStatus(OK)
    public void addEventToCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId) {
        if (!compilationService.addEventToCompilation(compId, eventId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @DeleteMapping("/{compId}/pin")
    @ResponseStatus(OK)
    public void removeCompilationFromHomePage(@PathVariable Long compId) {
        if (!compilationService.removeCompilationFromHomePage(compId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @PatchMapping("/{compId}/pin")
    @ResponseStatus(OK)
    public void addCompilationToHomePage(@PathVariable Long compId,
                                      @PathVariable Long eventId) {
        if (!compilationService.addEventToCompilation(compId, eventId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }
}
