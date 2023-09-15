package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.FullResponseItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ResponseItemRequestDto create(RequestItemRequestDto request, long userId);

    FullResponseItemRequestDto getById(long id, long userId);

    List<FullResponseItemRequestDto> getOwnItemRequests(long userId);

    List<FullResponseItemRequestDto> getAll(long userId, int from, int size);

}