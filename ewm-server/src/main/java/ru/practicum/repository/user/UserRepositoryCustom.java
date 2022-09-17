package ru.practicum.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.model.user.User;

import java.util.Set;

public interface UserRepositoryCustom {
    /**
     * Получить список пользователей с выбранными ids
     */
    Page<User> getUsersByIds(Set<Long> ids, Pageable pageable);
}
