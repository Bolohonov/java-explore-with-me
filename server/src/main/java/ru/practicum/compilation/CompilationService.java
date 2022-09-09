package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;

import java.util.Collection;

public interface CompilationService {
    Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
}
