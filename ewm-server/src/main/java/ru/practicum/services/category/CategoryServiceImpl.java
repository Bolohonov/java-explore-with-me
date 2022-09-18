package ru.practicum.services.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.mappers.category.CategoryMapper;
import ru.practicum.repository.category.CategoryRepository;
import ru.practicum.errors.ApiError;
import ru.practicum.services.event.EventServiceAdmin;

import java.util.*;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventServiceAdmin eventService;

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
    public Optional<CategoryDto> updateCategoryByAdmin(CategoryDto newCategory) {
        log.info("Получен запрос на обновление категории");
        validateCategoryForUpdate(newCategory);
        Category category = getCategoryFromRepository(newCategory.getId());
        category.setName(newCategory.getName());
        return of(CategoryMapper.toCategoryDto(category));
    }

    @Transactional
    @Override
    public Optional<CategoryDto> addCategoryByAdmin(Category newCategory) {
        log.info("Получен запрос на добавление категории");
        categoryRepository.save(newCategory);
        return of(CategoryMapper.toCategoryDto(newCategory));
    }

    @Transactional
    @Override
    public void deleteCategoryByAdmin(Long catId) {
        log.info("Получен запрос на удаление категории");
        Category categoryToDelete = getCategoryFromRepository(catId);
        if (!eventService
                .findEvents(null, null,
                        Arrays.asList(catId), null, null, 0, 1).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        categoryRepository.delete(categoryToDelete);
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }

    private void validateCategoryForUpdate(CategoryDto cat) {
        List<ApiError> errorsList = new ArrayList<>();
        if (cat.getId() == null) {
            errorsList.add(new ApiError(HttpStatus.BAD_REQUEST, String.format("Ошибка обновления категории"),
                    String.format("У категории в запросе отсутствует id"
                    )));
        }
        if (cat.getName() == null) {
            errorsList.add(new ApiError(HttpStatus.BAD_REQUEST, String.format("Ошибка обновления категории"),
                    String.format("У категории в запросе отсутствует name"
                    )));
        }
        if (!errorsList.isEmpty()) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Ошибка при обновлении категории",
                    String.format("Во время обновления категории произошли ошибки:"
                    ));
        }
    }

    private Category getCategoryFromRepository(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Ошибка запроса категории",
                        String.format("Категория с id %s не найдена. Проверьте id.", catId)
                ));
    }
}
