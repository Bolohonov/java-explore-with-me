package ru.practicum.mappers.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserDto;
import ru.practicum.model.user.dto.UserDtoWithRating;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static Collection<UserDto> toUserDto(Collection<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public static UserDtoWithRating toUserDtoWithRating(UserDto user, Long rating) {
        return new UserDtoWithRating(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getActivation() != null ? user.getActivation() : Boolean.FALSE,
                rating);
    }

    public static Collection<UserDtoWithRating> toUserDtoWithRatingColl(Map<UserDto, Long> users) {
        return users.entrySet().stream()
                .map((e) -> toUserDtoWithRating(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
