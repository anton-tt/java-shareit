package main.java.ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
@Slf4j
public class UserMapper {

    public User toUser(RequestUserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public ResponseUserDto toResponseUserDto(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUpdatedUser(User user, RequestUserDto userDto) {
        String userEmail = user.getEmail();
        String userDtoName = userDto.getName();
        String userDtoEmail = userDto.getEmail();

        if (userDtoName != null && !userDtoName.isBlank()) {
            user.setName(userDtoName);
            log.info("Имя пользователя изменено на {}.", userDtoName);
        }

        if (userDtoEmail != null && !userDtoEmail.isBlank()) {
            if (userEmail.equals(userDtoEmail)) {
                log.info("Адрес новой электронной почты и адрес старой одинаковые: {}. " +
                        "Обновление данных не требуется.", userDtoEmail);
            } else {
                user.setEmail(userDtoEmail);
                log.info("Эл.почта пользователя изменена на {}.", userDtoEmail);
            }
        }
        return user;
    }

}