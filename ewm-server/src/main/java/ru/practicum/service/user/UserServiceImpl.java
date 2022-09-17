package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.ApiError;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserDto;
import ru.practicum.model.user.mapper.UserMapper;
import ru.practicum.repository.user.UserRepository;

import java.util.*;

import static java.util.Optional.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final String ER_OBJ = "пользователь";

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> getUsers(Integer[] ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size,
                Sort.by("id").ascending());
        Set<Long> idSet = new HashSet<>();
        for (Integer id : ids) {
            idSet.add(id.longValue());
        }
        Iterable<User> usersPage = userRepository.getUsersByIds(idSet, pageRequest);
        Collection<User> users = new ArrayList<>();
        usersPage.forEach(users::add);
        return UserMapper.toUserDto(users);
    }

    @Transactional
    @Override
    public UserDto saveUser(User user) {
        user.setActivation(Boolean.TRUE);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> getUserById(Long userId) {
        return of(UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Пользователь не найден",
                        String.format("При выполнении %s не найден %s c id %s",
                                "getUserById", ER_OBJ, userId))
        )));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        if (!getUserById(userId).isPresent()) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Пользователь не найден",
                    String.format("При выполнении %s не найден %s c id %s",
                            "deleteUser", ER_OBJ, userId));
        }
        userRepository.deleteById(userId);
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }
}
