package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.FullResponseItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.service.ItemService;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseItemDto createItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                      @RequestBody RequestItemDto item) {
        log.info("");
        log.info("Добавление новой вещи: {}", item);
        return itemService.create(item, userId);
    }

    @GetMapping("/{id}")
    public FullResponseItemDto getItemById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                           @PathVariable long id) {
        log.info("");
        log.info("Получение данных вещи с id = {}", id);
        return itemService.getById(id, userId);
    }

    @GetMapping
    public List<FullResponseItemDto> getItemsOneUser(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("");
        log.info("Поиск всех вещей, созданных пользователем с id = {}", userId);
        //List<FullResponseItemDto> resultList = itemService.getItemsOneOwner(userId, from, size);
        //Collections.reverse(resultList);
        return itemService.getItemsOneOwner(userId, from, size);//resultList;
    }

    @PatchMapping("/{id}")
    public ResponseItemDto updateItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                      @PathVariable long id,
                                      @RequestBody RequestItemDto item) {
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
    public List<ResponseItemDto> searchItems(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @RequestParam String text,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("");
        log.info("Поиск вещей по определённому запросу пользователя");
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseCommentDto createComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                            @PathVariable long itemId,
                                            @RequestBody @Valid RequestCommentDto comment) {
        log.info("");
        log.info("Добавление для вещи с id = {} нового комментария: {}", itemId, comment);
        return itemService.createComment(itemId, comment, userId);
    }

}