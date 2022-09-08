package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.user.exceptions.UserNotFoundException;
import ru.practicum.user.repository.UserRepository;

import java.util.*;

import static java.util.Optional.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<User> getUsers(Integer[] ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size,
                Sort.by("id").ascending());
        Set<Long> idSet = new HashSet<>();
        for (Integer id : ids) {
            idSet.add(id.longValue());
        }
        Iterable<User> usersPage = userRepository.getUsersByIds(idSet, pageRequest);
        Collection<User> users = new ArrayList<>();
        usersPage.forEach(users::add);
        return users;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserById(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            return userRepository.findById(userId);
        } else {
            log.warn("пользователь с id {} не найден", userId);
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public Optional<User> updateUser(Long id, User user) {
        return of(userRepository.save(this.compareToUpdate(id, user)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        if (userRepository.existsById(userId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    private User compareToUpdate(Long id, User user) {
        if (userRepository.findById(id).isPresent()) {
            User oldUser = userRepository.findById(id).get();
            if (user.getName() == null) {
                user.setName(oldUser.getName());
            }
            if (user.getEmail() == null) {
                user.setEmail(oldUser.getEmail());
            }
            user.setId(id);
        }
        return user;
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }
}
