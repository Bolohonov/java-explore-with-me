package ru.practicum.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.Compilation;
import ru.practicum.event.Event;

import java.util.Set;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findCompilationsByPinned(Boolean pinned, Pageable pageable);
}
