package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.FullResponseItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseItemRequestDto createItemRequest(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                    @RequestBody @Valid RequestItemRequestDto request) {
        log.info("");
        log.info("От пользователя с id = {} добавление нового запроса на вещь: {}", userId, request);
        return itemRequestService.create(request, userId);
    }

    @GetMapping("/{requestId}")
    public FullResponseItemRequestDto getRequestsById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                      @PathVariable @Positive long requestId) {
        log.info("");
        log.info("Получение данных запроса по id = {} ", requestId);
        return itemRequestService.getById(requestId, userId);
    }

    @GetMapping
    public List<FullResponseItemRequestDto> getOwnItemRequests(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("");
        log.info("Поиск пользователем с id = {} всех своих запросов с ответами на них", userId);
        return itemRequestService.getOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public List<FullResponseItemRequestDto> getAllRequests(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        log.info("");
        log.info("Поиск всех запросов пользователей, на которые можно предложить свои вещи");
        return itemRequestService.getAll(userId, from, size);
    }

}