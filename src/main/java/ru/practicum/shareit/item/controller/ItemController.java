package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.LargeItemDto;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                              @RequestBody ItemDto item) {
        log.info("");
        log.info("Добавление новой вещи: {}", item);
        return itemService.create(item, userId);
    }

    @GetMapping("/{id}")
    public LargeItemDto getItemById(@RequestHeader(X_SHARER_USER_ID) long userId,
                               @PathVariable long id) {
        log.info("");
        log.info("Получение данных вещи с id = {}", id);
        return itemService.getById(id, userId);
    }

    @GetMapping
    public List<LargeItemDto> getItemsOneUser(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("");
        log.info("Поиск всех вещей, созданных пользователем с id = {}", userId);
        return itemService.getItemsOneOwner(userId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                              @PathVariable long id,
                              @RequestBody ItemDto item) {
        log.info("");
        log.info("Обновление данных вещи с id = {}: {}", id, item);
        return itemService.update(id, item, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                           @PathVariable long id) {
        log.info("");
        log.info("Удаление вещи c id = {}", id);
        itemService.delete(id, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(X_SHARER_USER_ID) long userId,
                                     @RequestParam String text) {
        log.info("");
        log.info("Поиск вещей по определённому запросу пользователя");
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseCommentDto createComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                            @PathVariable long itemId,
                                            @RequestBody RequestCommentDto comment) {
        log.info("");
        log.info("Добавление для вещи с id = {} нового комментария: {}", itemId, comment);
        return itemService.createComment(itemId, comment, userId);
    }

}