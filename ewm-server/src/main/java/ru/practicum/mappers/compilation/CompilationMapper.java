package ru.practicum.mappers.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.errors.ApiError;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.dto.CompilationAddDto;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.event.Event;
import ru.practicum.repository.event.EventRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompilationMapper {
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto toCompilationDto(Compilation compilation) {
        log.info("Получен запрос на конвертацию подборки");
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                compilation.getEvents()
        );
    }

    @Transactional
    public Collection<CompilationDto> toCompilationDto(Collection<Compilation> compilations) {
        return compilations.stream()
                .map(c -> toCompilationDto(c))
                .collect(Collectors.toList());
    }

    @Transactional
    public Compilation fromCompilationDto(CompilationDto compilationDto) {
        log.info("Получен запрос на конвертацию в подборку");
        return new Compilation(
                compilationDto.getId(),
                compilationDto.getTitle(),
                compilationDto.getPinned(),
                compilationDto.getEvents()
        );
    }

    @Transactional
    public Compilation fromCompilationAddDto(CompilationAddDto compilation) {
        log.info("Получен запрос на конвертацию в подборку");
        Set< Event > events = new HashSet<>();
        compilation.getEvents()
                .stream()
                .forEach(id -> events.add(eventRepository.findById(id).orElseThrow(
                        () -> new ApiError(HttpStatus.BAD_REQUEST, "Получен несуществующий id",
                                String.format("Получен несуществующий id %s события " +
                                        "при попытке добавить подборку", id)
                        ))));
        return new Compilation(
                compilation.getTitle(),
                compilation.getPinned(),
                events
        );
    }
}
