package dev.bolohonov.controllers.publicAPI.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import dev.bolohonov.services.compilation.CompilationService;
import dev.bolohonov.model.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(OK)
    public Collection<CompilationDto> findCompilations(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compilationId}")
    @ResponseStatus(OK)
    public Optional<CompilationDto> findCompilationById(@PathVariable Long compilationId) {
        return compilationService.getCompilationById(compilationId);
    }
}
