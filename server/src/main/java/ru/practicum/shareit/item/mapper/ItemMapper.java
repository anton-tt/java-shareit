package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.FullResponseItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
@Slf4j
public class ItemMapper {

    public Item toItem(RequestItemDto itemDto, User owner) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .build();
    }

    public ResponseItemDto toResponseItemDto(Item item) {
        return ResponseItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .requestId(item.getItemRequest() != null ? item.getItemRequest().getId() : 0)
                .build();
    }

    public FullResponseItemDto toFullResponseItemDto(Item item) {
        return FullResponseItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public Item toUpdatedItem(Item item, RequestItemDto requestItemDto) {
        String itemDtoName = requestItemDto.getName();
        String itemDtoDescription = requestItemDto.getDescription();
        Boolean isAvailable = requestItemDto.getAvailable();

        if (itemDtoName != null && !itemDtoName.isEmpty()) {
            item.setName(requestItemDto.getName());
            log.info("Имя вещи изменено на {}.", itemDtoName);
        }
        if (itemDtoDescription != null && !itemDtoDescription.isEmpty()) {
            item.setDescription(requestItemDto.getDescription());
            log.info("Описание вещи изменено на {}.", itemDtoDescription);
        }
        if (isAvailable != null) {
            item.setAvailable(isAvailable);
            log.info("Статус бронирования вещи изменён на {}.", isAvailable);
        }
        return item;
    }

}