package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.event.Event;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.UserService;
import ru.practicum.user.exceptions.UserNotFoundException;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class Validation {
    private final UserRepository userRepository;

    public void validateUserActivation(Long userId) {
        if (!userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"))
                .isActivation()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateEventDate(EventShortDto event) {
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateEventDate(Event event) {
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
