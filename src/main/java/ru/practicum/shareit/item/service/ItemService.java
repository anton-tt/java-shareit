package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.LargeItemDto;
import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long userId);

    LargeItemDto getById(long id, long ownerId);

    List<LargeItemDto> getItemsOneOwner(long userId);

    ItemDto update(long id, ItemDto itemDto, long userId);

    void delete(long id, long userId);

    List<ItemDto> search(String text);

    CommentDto createComment(long itemId, CommentDto commentDto, long userId);

}