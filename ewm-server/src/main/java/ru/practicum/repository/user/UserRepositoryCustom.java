package ru.practicum.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.model.user.User;

import java.util.Set;

public interface UserRepositoryCustom {
    Page<User> getUsersByIds(Set<Long> ids, Pageable pageable);
}
