package main.java.ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.groups.Create;
import ru.practicum.shareit.utils.groups.Update;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseUserDto createUser(@RequestBody @Validated(Create.class) RequestUserDto userDto) {
        log.info("");
        log.info("Добавление нового пользователя: {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping("/{id}")
    public ResponseUserDto getUserById(@PathVariable long id) {
        log.info("");
        log.info("Получение данных пользователя с id = {}", id);
        return userService.getById(id);
    }

    @GetMapping
    public List<ResponseUserDto> getAllUsers() {
        log.info("");
        log.info("Поиск всех пользователей");
        return userService.getAll();
    }

    @PatchMapping("/{id}")
    public ResponseUserDto updateUser(@PathVariable long id,
                                      @RequestBody @Validated(Update.class) RequestUserDto userDto) {
        log.info("");
        log.info("Обновление данных пользователя с id = {}: {}", id, userDto);
        return userService.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("");
        log.info("Удаление всех данных пользователя c id = {}", id);
        userService.delete(id);
    }

}