package main.java.ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;

import java.util.List;

public interface UserService {

    ResponseUserDto create(RequestUserDto userDto);

    ResponseUserDto getById(long id);

    List<ResponseUserDto> getAll();

    ResponseUserDto update(long id, RequestUserDto userDto);

    void delete(long id);

}