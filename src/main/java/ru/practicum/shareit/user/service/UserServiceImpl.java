package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info("Данные пользователя добавлены в БД: {}.", user);
        UserDto createdUserDto = UserMapper.toUserDto(user);
        log.info("Новый пользователь создан: {}.", createdUserDto);
        return createdUserDto;
    }

    @Override
    public UserDto getById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
        log.info("Пользователь найден в БД: {}.", user);
        UserDto userDto = UserMapper.toUserDto(user);
        log.info("Данные пользователя получены: {}.", userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Получение данных всех пользователей из БД.");
        List<UserDto> allUserDto = userRepository.findAll()
            .stream()
            .map(UserMapper::toUserDto)
            .collect(Collectors.toList());
        log.info("Сформирован список всех пользователей в количестве: {}.", allUserDto.size());
        return allUserDto;
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
        log.info("Пользователь найден в БД: {}.", updatedUser);
        User newDataUser = userRepository.save(UserMapper.toUpdatedUser(updatedUser, userDto));
        log.info("Данные пользователя обновлены в БД: {}.", newDataUser);
        UserDto updatedUserDto = UserMapper.toUserDto(newDataUser);
        log.info("Данные пользователя обновлены: {}.", updatedUserDto);
        return updatedUserDto;
    }

    @Override
    public void delete(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
        log.info("Пользователь найден в БД: {}.", user);
        userRepository.delete(user);
        log.info("Все данные пользователя удалены.");
    }

}