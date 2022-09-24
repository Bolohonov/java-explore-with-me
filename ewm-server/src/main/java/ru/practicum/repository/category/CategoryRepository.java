package ru.practicum.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Найти категории с названием name
     */
    Category findAllByName(String name);
}
