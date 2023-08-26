package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.LargeItemDto;
import ru.practicum.shareit.item.model.Item;

@Slf4j
public class ItemMapper {

    private ItemMapper() {

    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.isAvailable())
            .requestId(item.getRequestId() != null ? item.getRequestId() : null)
            .build();
    }

    public static Item toItem(ItemDto itemDto, long ownerId) {
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
                    .ownerId(ownerId)
                    .requestId(itemDto.getRequestId() != null ? itemDto.getRequestId() : null)
                    .build();
        }
    }

    public static LargeItemDto toLargeItemDto(Item item) {
        return LargeItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.isAvailable())
            .requestId(item.getRequestId() != null ? item.getRequestId() : null)
            .build();
    }

    public static Item toUpdatedItem(Item updatedItem, ItemDto newItemDto) {
        String itemDtoName = newItemDto.getName();
        String itemDtoDescription = newItemDto.getDescription();
        Boolean isAvailable = newItemDto.getAvailable();

        if (itemDtoName != null && !itemDtoName.isEmpty()) {
            updatedItem.setName(newItemDto.getName());
            log.info("Имя вещи изменено на {}.", itemDtoName);
        }
        if (itemDtoDescription != null && !itemDtoDescription.isEmpty()) {
            updatedItem.setDescription(newItemDto.getDescription());
            log.info("Описание вещи изменено на {}.", itemDtoDescription);
        }
        if (isAvailable != null) {
            updatedItem.setAvailable(isAvailable);
            log.info("Статус бронирования вещи изменён на {}.", isAvailable);
        }
        return updatedItem;
    }

}