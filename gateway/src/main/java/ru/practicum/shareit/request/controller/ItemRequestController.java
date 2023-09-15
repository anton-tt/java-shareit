package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                    @RequestBody @Valid RequestItemRequestDto request) {
        log.info("");
        log.info("Gateway: поступил запрос на создание запроса на вещь.");
        return itemRequestClient.create(userId, request);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestsById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                  @PathVariable @Positive long requestId) {
        log.info("");
        log.info("Gateway: поступил запрос на получение данных запроса на вещь.");
        return itemRequestClient.getById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("");
        log.info("Gateway: поступил запрос от пользователя на получение всех своих запросов с ответами на них");
        return itemRequestClient.getOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                 @RequestParam(defaultValue = "0")  @Min(0) int from,
                                                 @RequestParam(defaultValue = "10")  @Min(1) int size) {
        log.info("");
        log.info("Gateway: поступил запрос на поиск всех запросов пользователей, на которые можно предложить свои вещи");
        return itemRequestClient.getAll(userId, from, size);
    }

}