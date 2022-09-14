package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.ApiError;
import ru.practicum.event.EventService;

import java.util.*;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryMainService implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventService eventService;

    @Transactional(readOnly = true)
    @Override
    public Collection<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("Получен запрос на вывод списка категорий");
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size);
        return CategoryMapper.toCategoryDto(categoryRepository.findAll(pageRequest));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CategoryDto> getCategoryById(Long catId) {
        log.info("Получен запрос на вывод категории");
        return of(CategoryMapper
                .toCategoryDto(getCategoryFromRepository(catId)));
    }

    @Transactional
    @Override
    public Optional<CategoryDto> updateCategoryByAdmin(Category newCategory) {
        log.info("Получен запрос на обновление категории");
        Category category = getCategoryFromRepository(newCategory.getId());
        try {
            category.setName(newCategory.getName());
        } catch (DuplicateKeyException e) {
            new ApiError(HttpStatus.BAD_REQUEST, "Ошибка обновления названия категории",
                    String.format("Название %s уже существует", newCategory.getName()));
        }
        return of(CategoryMapper.toCategoryDto(category));
    }

    @Transactional
    @Override
    public Optional<CategoryDto> addCategoryByAdmin(Category newCategory) {
        log.info("Получен запрос на добавление категории");
        try {
            categoryRepository.save(newCategory);
        } catch (DuplicateKeyException e) {
            new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return of(CategoryMapper.toCategoryDto(newCategory));
    }

    @Transactional
    @Override
    public void deleteCategoryByAdmin(Long catId) {
        log.info("Получен запрос на удаление категории");
        Category categoryToDelete = getCategoryFromRepository(catId);
        if (!eventService
                .findEventsByAdmin(null, null,
                        Arrays.asList(catId),  null, null, 0, 1).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        categoryRepository.delete(categoryToDelete);
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }

    private Category getCategoryFromRepository(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
    }
}
