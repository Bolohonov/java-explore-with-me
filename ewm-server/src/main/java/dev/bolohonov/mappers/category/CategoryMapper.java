package dev.bolohonov.mappers.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import dev.bolohonov.model.category.Category;
import dev.bolohonov.model.category.dto.CategoryDto;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Collection<CategoryDto> toCategoryDto(Iterable<Category> categories) {
        Collection<CategoryDto> dtos = new ArrayList<>();
        for (Category category : categories) {
            dtos.add(toCategoryDto(category));
        }
        return dtos;
    }
}
