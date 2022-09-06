package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;

import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryMainService implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size);
        return CategoryMapper.toCategoryDto(categoryRepository.findAll(pageRequest));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CategoryDto> getCategoryById(Long catId) {
        return of(CategoryMapper
                .toCategoryDto(categoryRepository.findById(catId)
                        .orElseThrow(() -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                        })));
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }
}
