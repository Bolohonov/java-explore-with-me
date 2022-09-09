package ru.practicum.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.Compilation;
import ru.practicum.event.Event;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
