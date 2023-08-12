package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
            .name(item.getName())
            .description(item.getDescription())
            .available(item.isAvailable())
            .request(item.getRequest() != null ? item.getRequest() : null)
            .build();
    }

}