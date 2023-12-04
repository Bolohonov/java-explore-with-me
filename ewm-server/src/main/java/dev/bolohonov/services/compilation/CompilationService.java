package dev.bolohonov.services.compilation;

import dev.bolohonov.model.compilation.dto.CompilationAddDto;
import dev.bolohonov.model.compilation.dto.CompilationDto;

import java.util.Collection;
import java.util.Optional;

public interface CompilationService {
    /**
     * Получить список подборок
     * прикрепленных/или нет/ к главной странице
     */
    Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    /**
     * Получить подборку по id
     */
    Optional<CompilationDto> getCompilationById(Long compilationId);

    /**
     * Добавить подборку
     */
    Optional<CompilationDto> addCompilation(CompilationAddDto compilation);

    /**
     * Удалить подборку
     */
    void deleteCompilation(Long compilationId);

    /**
     * Удалить событие из подборки
     */
    boolean removeEventFromCompilation(Long compId, Long eventId);

    /**
     * Добавить событие в подборку
     */
    boolean addEventToCompilation(Long compId, Long eventId);

    /**
     * Удалить подборку с главной страницы
     */
    boolean removeCompilationFromHomePage(Long compId);

    /**
     * Добавить подборку на главную страницу
     */
    boolean addCompilationToHomePage(Long compId);
}
