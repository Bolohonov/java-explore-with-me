package dev.bolohonov.controllers.privateAPI.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import dev.bolohonov.model.user.User;
import dev.bolohonov.services.user.UserService;
import dev.bolohonov.model.user.dto.UserDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(OK)
    public Collection<UserDto> getUsers(@RequestParam Integer[] ids,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                        @Positive @RequestParam(defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(OK)
    public UserDto saveNewUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public UserDto findUserById(@PathVariable Long id) {
        return userService.getUserById(id).get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
