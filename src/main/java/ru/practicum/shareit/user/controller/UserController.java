package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto user) {
        log.info("Добавление нового пользователя: {}", user);
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        log.info("Получение данных пользователя с id = {}", id);
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Поиск всех пользователей");
        List<UserDto> usersList = userService.getAll();
        log.info("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto user) {
        log.info("Обновление данных пользователя: {}", user);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("Удаление пользователя c id = {}", id);
        userService.delete(id);
    }

}