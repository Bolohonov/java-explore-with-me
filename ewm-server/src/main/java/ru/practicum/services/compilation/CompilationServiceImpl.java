package ru.practicum.services.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.dto.CompilationAddDto;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.mappers.compilation.CompilationMapper;
import ru.practicum.repository.compilation.CompilationRepository;
import ru.practicum.errors.ApiError;
import ru.practicum.repository.event.EventRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.debug("Получен запрос в сервис на получение списка подборок");
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size,
                Sort.by("id").ascending());
        Iterable<Compilation> compilationsPage = compilationRepository.findCompilationsByPinned(pinned, pageRequest);
        Collection<Compilation> compilations = new ArrayList<>();
        compilationsPage.forEach(compilations::add);
        return compilationMapper.toCompilationDto(compilations);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CompilationDto> getCompilationById(Long compilationId) {
        log.debug("Получен запрос в сервис на получение подборки с id {}", compilationId);
        return of(compilationMapper.toCompilationDto(getById(compilationId)));
    }

    @Transactional
    @Override
    public Optional<CompilationDto> addCompilation(CompilationAddDto compilation) {
        log.debug("Получен запрос в сервис на добавление подборки");
        return of(compilationMapper.toCompilationDto(compilationRepository
                .save(compilationMapper
                        .fromCompilationAddDto(compilation))));
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compilationId) {
        log.debug("Получен запрос в сервис на удаление подборки");
        compilationRepository.delete(getById(compilationId));
    }

    @Transactional
    @Override
    public boolean removeEventFromCompilation(Long compId, Long eventId) {
        log.debug("Получен запрос в сервис на удаление события с id {} из подборки {}", eventId, compId);
        return getById(compId).getEvents().remove(eventRepository.findById(eventId).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Не найдено событие",
                        String.format("Не найдено событие с id %s при попытке добавления в подборку", eventId)
                )));
    }

    @Transactional
    @Override
    public boolean addEventToCompilation(Long compId, Long eventId) {
        log.debug("Получен запрос в сервис на добавление события с id {} в подборку {}", eventId, compId);
        return getById(compId).getEvents().add(eventRepository.findById(eventId).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Не найдено событие",
                        String.format("Не найдено событие с id %s при попытке добавления в подборку", eventId)
                )));
    }

    @Transactional
    @Override
    public boolean removeCompilationFromHomePage(Long compId) {
        log.debug("Получен запрос в сервис на удаление подборки с id {} с главной страницы", compId);
        compilationRepository.findById(compId).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Подборка не найдена",
                        String.format("Подборка с id %s не найдена", compId))
        ).setPinned(Boolean.FALSE);
        return true;
    }

    @Transactional
    @Override
    public boolean addCompilationToHomePage(Long compId) {
        log.debug("Получен запрос в сервис на добавление подборки с id {} на главную страницу", compId);
        compilationRepository.findById(compId).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Подборка не найдена",
                        String.format("Подборка с id %s не найдена", compId))
        ).setPinned(Boolean.TRUE);
        return true;
    }

    private Compilation getById(Long id) {
        return compilationRepository.findById(id).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Подборка не найдена",
                        String.format("Подборка с id %s не найдена", id))
        );
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }
}
