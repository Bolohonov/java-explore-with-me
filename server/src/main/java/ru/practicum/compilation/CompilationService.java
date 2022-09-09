package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;

import java.util.Collection;
import java.util.Optional;

public interface CompilationService {
    Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
    Optional<CompilationDto> getCompilationById(Long compilationId);
    Optional<CompilationDto> addCompilation(Compilation compilation);
    void deleteCompilation(Long compilationId);
    boolean removeEventFromCompilation(Long compId, Long eventId);
    boolean addEventToCompilation(Long compId, Long eventId);
    boolean removeCompilationFromHomePage(Long compId);
    boolean addCompilationToHomePage(Long compId);
}
