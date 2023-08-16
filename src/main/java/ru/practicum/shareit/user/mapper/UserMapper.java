package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();
    }

    public static User toUser(UserDto userDto) {
        String userDtoName = userDto.getName();
        String userDtoMail = userDto.getEmail();

        if (userDtoName == null || userDtoName.isEmpty()) {
            throw new ValidationException("У пользователя отсутствует имя! Операцию выполнить невозможно.");
        } else if (userDtoMail == null || !userDtoMail.contains("@")) {
            throw new ValidationException("У пользователя электронная почта отсутствует или не соответствует " +
                    "правильному формату! Операцию выполнить невозможно.");
        } else {
            return User.builder()
                    .id(userDto.getId())
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .build();
        }
    }

}