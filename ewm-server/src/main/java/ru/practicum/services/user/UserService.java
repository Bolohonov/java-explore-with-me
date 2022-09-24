package ru.practicum.services.user;

import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    /**
     * Получить список пользователей
     */
    Collection<UserDto> getUsers(Integer[] ids, Integer from, Integer size);

    /**
     * Добавить пользователя
     */
    UserDto saveUser(User user);

    /**
     * Получить пользователя по id
     */
    Optional<UserDto> getUserById(Long userId);

    /**
     * Удалить пользователя
     */
    void deleteUser(Long userId);
}
