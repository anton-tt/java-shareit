package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.FullResponseItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class ItemRequestMapper {

    public ItemRequest toItemRequest(RequestItemRequestDto requestDto, User user, LocalDateTime currentMoment) {
        return ItemRequest.builder()
                .description(requestDto.getDescription())
                .created(currentMoment)
                .requestor(user)
                .build();
    }

    public ResponseItemRequestDto toResponseItemRequestDto(ItemRequest request, ResponseUserDto requestorDto) {
        return ResponseItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .requestor(requestorDto)
                .build();
    }

    public FullResponseItemRequestDto toFullResponseItemRequestDto(ItemRequest request) {
        return FullResponseItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

}