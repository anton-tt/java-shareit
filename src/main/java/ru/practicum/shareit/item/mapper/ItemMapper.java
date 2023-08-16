package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.exception.ValidationException;
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
        String itemDtoName = itemDto.getName();
        String itemDtoDescription = itemDto.getDescription();
        Boolean isAvailable = itemDto.getAvailable();

        if (itemDtoName == null || itemDtoName.isEmpty()) {
            throw new ValidationException("У вещи отсутствует название! Операцию выполнить невозможно.");
        } else if (itemDtoDescription == null || itemDtoDescription.isEmpty()) {
            throw new ValidationException("У вещи отсутствует описание! Операцию выполнить невозможно.");
        } else if (isAvailable == null) {
            throw new ValidationException("У вещи не задана возможность бронирования! Операцию выполнить невозможно.");
        } else {
            return Item.builder()
                    .id(itemDto.getId())
                    .name(itemDtoName)
                    .description(itemDtoDescription)
                    .available(isAvailable)
                    .request(itemDto.getRequest() != null ? itemDto.getRequest() : null)
                    .build();
        }

    }

    public static Item toUpdatedItem(Item updatedItem, ItemDto newItemDto) {
        String itemDtoName = newItemDto.getName();
        String itemDtoDescription = newItemDto.getDescription();
        Boolean isAvailable = newItemDto.getAvailable();

        if (itemDtoName != null && !itemDtoName.isEmpty()) {
            updatedItem.setName(newItemDto.getName());
        }
        if (itemDtoDescription != null && !itemDtoDescription.isEmpty()) {
            updatedItem.setDescription(newItemDto.getDescription());
        }
        if (isAvailable != null) {
            updatedItem.setAvailable(isAvailable);
        }
        return updatedItem;

    }

}