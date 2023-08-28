package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.FullResponseItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import java.util.List;

public interface ItemService {

    ResponseItemDto create(RequestItemDto itemDto, long userId);

    FullResponseItemDto getById(long id, long ownerId);

    List<FullResponseItemDto> getItemsOneOwner(long userId);

    ResponseItemDto update(long id, RequestItemDto itemDto, long userId);

    void delete(long id, long userId);

    List<ResponseItemDto> search(String text);

    ResponseCommentDto createComment(long itemId, RequestCommentDto commentDto, long userId);

}