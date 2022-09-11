package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.of;

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

    @Override
    public Optional<CompilationDto> getCompilationById(Long compilationId) {
        return of(CompilationMapper.toCompilationDto(getById(compilationId)));
    }

    @Override
    public Optional<CompilationDto> addCompilation(Compilation compilation) {
        return of(CompilationMapper.toCompilationDto(compilationRepository.save(compilation)));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compilationRepository.delete(getById(compilationId));
    }

    @Override
    public boolean removeEventFromCompilation(Long compId, Long eventId) {
        return getById(compId).getEvents_in().remove(eventId);
    }

    @Override
    public boolean addEventToCompilation(Long compId, Long eventId) {
        return getById(compId).getEvents_in().add(eventId);
    }

    @Override
    public boolean removeCompilationFromHomePage(Long compId) {
        getById(compId).setPinned(Boolean.FALSE);
        return true;
    }

    @Override
    public boolean addCompilationToHomePage(Long compId) {
        getById(compId).setPinned(Boolean.TRUE);
        return true;
    }

    private Compilation getById(Long id) {
        return compilationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }

}
