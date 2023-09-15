package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.utils.groups.Create;
import ru.practicum.shareit.utils.groups.Update;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @RequestBody @Validated(Create.class) RequestItemDto item) {
        log.info("");
        log.info("Gateway: поступил запрос на создание вещи.");
        return itemClient.create(userId, item);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                              @PathVariable long id) {
        log.info("");
        log.info("Gateway: поступил запрос на получение данных пользователя.");
        return itemClient.getById(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsOneUser(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                  @RequestParam(defaultValue = "0") @Min(0) int from,
                                                  @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("");
        log.info("Поиск всех вещей, созданных пользователем с id = {}", userId);
        return itemClient.getItemsOneUser(userId, from, size);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable long id,
                                             @RequestBody @Validated(Update.class) RequestItemDto item) {
        log.info("");
        log.info("Gateway: поступил запрос на обновление данных вещи.");
        return itemClient.update(userId, id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable long id) {
        log.info("");
        log.info("Gateway: поступил запрос на удаление всех данных вещи.");
        return itemClient.delete(userId, id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(X_SHARER_USER_ID) long userId,
                                              @RequestParam String text,
                                              @RequestParam(defaultValue = "0") @Min(0) int from,
                                              @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("");
        log.info("Gateway: поступил запрос на поиск вещей по названию и описанию.");
        return itemClient.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                @PathVariable long itemId,
                                                @RequestBody @Valid RequestCommentDto comment) {
        log.info("");
        log.info("Gateway: поступил запрос на создание комментария для вещи.");
        return itemClient.createComment(userId, itemId, comment);
    }

}