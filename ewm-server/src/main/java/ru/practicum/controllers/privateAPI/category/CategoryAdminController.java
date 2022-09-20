package ru.practicum.controllers.privateAPI.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.Category;
import ru.practicum.services.category.CategoryService;
import ru.practicum.model.category.dto.CategoryDto;

import javax.validation.Valid;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PatchMapping
    @ResponseStatus(OK)
    public Optional<CategoryDto> updateCategory(@RequestBody CategoryDto category) {
        return categoryService.updateCategoryByAdmin(category);
    }

    @PostMapping
    @ResponseStatus(OK)
    public Optional<CategoryDto> addCategory(@RequestBody @Valid Category category) {
        return categoryService.addCategoryByAdmin(category);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(OK)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategoryByAdmin(catId);
    }
}
