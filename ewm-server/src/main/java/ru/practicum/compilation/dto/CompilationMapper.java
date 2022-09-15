package ru.practicum.compilation.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.Compilation;
import ru.practicum.error.ApiError;
import ru.practicum.event.Event;
import ru.practicum.event.repository.EventRepository;

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
                compilation.getEvents().stream()
                        .map(Event::getId).collect(Collectors.toList())
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
        Set<Event> events = new HashSet<>();
        compilationDto.getEvents()
                .stream()
                .forEach(id -> events.add(eventRepository.findById(id).orElseThrow(
                        () -> new ApiError(HttpStatus.BAD_REQUEST, "Получен несуществующий id",
                                String.format("Получен несуществующий id %s события " +
                                        "при попытке добавить подборку", id)
                        ))));
        return new Compilation(
                compilationDto.getId(),
                compilationDto.getTitle(),
                compilationDto.getPinned(),
                events
        );
    }
}
