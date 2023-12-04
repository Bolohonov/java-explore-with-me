package dev.bolohonov.services.user;

import dev.bolohonov.model.user.User;
import dev.bolohonov.model.user.dto.UserDto;

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
