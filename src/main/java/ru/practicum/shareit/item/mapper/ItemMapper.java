package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.isAvailable())
            .request(item.getRequest() != null ? item.getRequest() : null)
            .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
            .id(itemDto.getId())
            .name(itemDto.getName())
            .description(itemDto.getDescription())
            .available(itemDto.isAvailable())
            .request(itemDto.getRequest() != null ? itemDto.getRequest() : null)
            .build();
    }

    public static Item toUpdatedItem(Item updatedItem, ItemDto newItemDto) {
        updatedItem.setName(newItemDto.getName());
        updatedItem.setDescription(newItemDto.getDescription());
        updatedItem.setAvailable(newItemDto.isAvailable());
        return updatedItem;
    }

}