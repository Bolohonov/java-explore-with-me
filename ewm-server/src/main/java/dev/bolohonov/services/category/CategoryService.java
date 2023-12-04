package dev.bolohonov.services.category;

import dev.bolohonov.model.category.dto.CategoryDto;
import dev.bolohonov.model.category.Category;

import java.util.Collection;
import java.util.Optional;

public interface CategoryService {
    /**
     * Получить список категорий
     */
    Collection<CategoryDto> getCategories(Integer from, Integer size);

    /**
     * Получить категорию по id
     */
    Optional<CategoryDto> getCategoryById(Long catId);

    /**
     * Обновить категорию администратором
     */
    Optional<CategoryDto> updateCategoryByAdmin(CategoryDto category);

    /**
     * Добавить категорию администратором
     */
    Optional<CategoryDto> addCategoryByAdmin(Category category);

    /**
     * Удалить категорию администратором
     */
    void deleteCategoryByAdmin(Long catId);
}
