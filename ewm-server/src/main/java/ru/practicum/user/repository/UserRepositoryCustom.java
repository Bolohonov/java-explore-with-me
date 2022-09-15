package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.user.User;

import java.util.Set;

public interface UserRepositoryCustom {
    Page<User> getUsersByIds(Set<Long> ids, Pageable pageable);
}
