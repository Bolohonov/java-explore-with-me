package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(OK)
    public Collection<User> getAllUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public User saveNewUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public User patchedUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user).get();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public User findUserById(@PathVariable Long id) {
        return userService.getUserById(id).get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(OK)
    public Collection<RequestDto> findUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/{userId}/requests/{eventId}")
    @ResponseStatus(OK)
    public Optional<RequestDto> addRequest(@PathVariable Long userId,
                                           @PathVariable Long eventId) {
        return requestService.addNewRequest(userId, eventId);
    }

}
