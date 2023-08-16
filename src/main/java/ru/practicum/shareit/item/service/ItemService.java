package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long userId);

    ItemDto getById(long id);

    List<ItemDto> getItemsOneOwner(long userId);

    ItemDto update(long id, ItemDto itemDto, long userId);

    void delete(long id, long userId);

    List<ItemDto> search(String text);

}