package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto  userDto) {
        User user = userStorage.create(UserMapper.toUser(userDto));
        UserDto createdUserDto = UserMapper.toUserDto(user);
        log.info("Новый пользователь (userDto) создан: {}.", createdUserDto);
        return createdUserDto;
    }

    @Override
    public UserDto getById(long id) {
        UserDto userDto = UserMapper.toUserDto(userStorage.getById(id));
        log.info("Данные пользователя (userDto) получены: {}.", userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> allUserDto = userStorage.getAll()
            .stream()
            .map(UserMapper::toUserDto)
            .collect(Collectors.toList());
        log.info("Сформирован список всех пользователей (allUserDto).");
        return allUserDto;
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userStorage.update(UserMapper.toUser(userDto));
        UserDto updatedUserDto = UserMapper.toUserDto(user);
        log.info("Данные пользователя (userDto) обновлены: {}.", updatedUserDto);
        return updatedUserDto;
    }

    @Override
    public void delete(UserDto userDto) {
        userStorage.delete(UserMapper.toUser(userDto));
        log.info("Все данные пользователя (userDto) удалены.");
    }

}