package dev.bolohonov.mappers.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import dev.bolohonov.model.user.User;
import dev.bolohonov.model.user.dto.UserDto;
import dev.bolohonov.model.user.dto.UserWithRatingDto;

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

    public static UserWithRatingDto toUserDtoWithRating(UserDto user, Long rating) {
        return new UserWithRatingDto(user.getId(),
                user.getName(),
                user.getEmail(),
                rating);
    }

    public static Collection<UserWithRatingDto> toUserDtoWithRating(Map<UserDto, Long> users) {
        return users.entrySet().stream()
                .map((e) -> toUserDtoWithRating(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
