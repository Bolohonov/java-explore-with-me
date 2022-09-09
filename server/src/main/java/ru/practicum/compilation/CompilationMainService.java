package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.Event;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationMainService implements CompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size,
                Sort.by("id").ascending());
        Iterable<Compilation> compilationsPage = compilationRepository.findCompilationsByPinned(pinned, pageRequest);
        Collection<Compilation> compilations = new ArrayList<>();
        compilationsPage.forEach(compilations::add);
        return CompilationMapper.toCompilationDto(compilations);
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }

}
