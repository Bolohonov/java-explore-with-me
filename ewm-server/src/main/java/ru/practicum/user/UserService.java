package ru.practicum.user;

import ru.practicum.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Collection<UserDto> getUsers(Integer[] ids, Integer from, Integer size);

    UserDto saveUser(User user);

    Optional<UserDto> getUserById(Long userId);

    void deleteUser(Long userId);
}
