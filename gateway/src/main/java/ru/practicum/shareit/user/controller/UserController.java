package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.utils.groups.Create;
import ru.practicum.shareit.utils.groups.Update;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(Create.class) RequestUserDto userDto) {
        log.info("");
        log.info("Gateway: поступил запрос на создание пользователя.");
        return userClient.create(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        log.info("");
        log.info("Gateway: поступил запрос на получение данных пользователя.");
        return userClient.getById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("");
        log.info("Gateway: поступил запрос на поиск всех пользователей.");
        return userClient.getAll();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id,
                                             @RequestBody @Validated(Update.class) RequestUserDto userDto) {
        log.info("");
        log.info("Gateway: поступил запрос на обновление данных пользователя.");
        return userClient.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        log.info("");
        log.info("Gateway: поступил запрос на удаление всех данных пользователя.");
        return userClient.delete(id);
    }

}