package ru.practicum.shareit.user.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
public class UserMapper {

    private UserMapper() {

    }

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

    public static User toUpdatedUser(User updatedUser, UserDto userDto) {
        String updatedUserEmail = updatedUser.getEmail();
        String userDtoName = userDto.getName();
        String userDtoEmail = userDto.getEmail();

        if (userDtoName != null) {
            updatedUser.setName(userDtoName);
            log.info("Имя пользователя изменено на {}.", userDtoName);
        }

        if (userDtoEmail != null) {
            if (!userDtoEmail.contains("@")) {
                throw new ValidationException(String.format("У пользователя некорректный новый адрес " +
                        "электронной почты: %s. Обновить данные невозможно!", userDtoEmail));
            } else if (updatedUserEmail.equals(userDtoEmail)) {
                log.info("Адрес новой электронной почты и адрес старой одинаковые: {}. " +
                        "Обновление данных не требуется.", userDtoEmail);
            } else {
                updatedUser.setEmail(userDtoEmail);
                log.info("Эл.почта пользователя изменена на {}.", userDtoEmail);
            }
        }
        return updatedUser;
    }

}