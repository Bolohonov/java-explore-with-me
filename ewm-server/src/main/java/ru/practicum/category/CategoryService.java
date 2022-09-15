package ru.practicum.category;

import ru.practicum.category.dto.CategoryDto;

import java.util.Collection;
import java.util.Optional;

public interface CategoryService {
    Collection<CategoryDto> getCategories(Integer from, Integer size);

    Optional<CategoryDto> getCategoryById(Long catId);

    Optional<CategoryDto> updateCategoryByAdmin(CategoryDto category);

    Optional<CategoryDto> addCategoryByAdmin(Category category);

    void deleteCategoryByAdmin(Long catId);
}
