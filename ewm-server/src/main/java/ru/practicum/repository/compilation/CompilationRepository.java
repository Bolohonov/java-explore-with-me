package ru.practicum.repository.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    /**
     * Найти подборки прикрепленные к главной странице
     */
    Page<Compilation> findCompilationsByPinned(Boolean pinned, Pageable pageable);
}
